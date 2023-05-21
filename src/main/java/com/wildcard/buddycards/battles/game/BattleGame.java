package com.wildcard.buddycards.battles.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.wildcard.buddycards.container.BattleContainer;
import com.wildcard.buddycards.item.BuddycardItem;

import net.minecraft.world.item.ItemStack;

//IMPORTANT - This is the ONLY class in battles.game that will use classes outside the battles.game package.
//ALSO IMPORTANT - Any methods in this class take slots ranging 1-6, with slot 012=P1 and 345=P2
public class BattleGame {
    
    private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
    
    public BattleContainer container;
    private boolean gameEnded = false;
    private List<BattleAttack> savedAttacks = new ArrayList<>();
    private List<BuddycardItem> items = new ArrayList<>(6);
    public final BattleCardState[] state = new BattleCardState[6];
    /** Actively changing turn power. */
    public final int[] turnPower = new int[6];
    
    public BattleGame(BattleContainer container) {
        this.container = container;
        for (int i = 0; i < 6; i++) {
            if (container.getItem(i).getItem() instanceof BuddycardItem item) items.add(item);
            else items.add(null);
        }
    }
    
    public boolean hasGameEnded() {
        return gameEnded;
    }
    
    /** begins the game. does NOT reset state, make a new BattleGame for that */
    public void beginGame() {
        LOGGER.info("Game Start");
        container.tryDrawCard(true);
        container.tryDrawCard(true);
        container.tryDrawCard(false);
        container.tryDrawCard(false);
        for (int i = 0; i < 6; i++) state[i] = new BattleCardState(0);
        startTurn(); //do not use nextTurn(), turn 1 player already chosen
    }
    
    /** starts a turn */
    public void startTurn() {
        LOGGER.info("Player " + player() + "'s turn!");
        //allocate energy
        if (isP1()) container.energy1 = Math.min(container.energy1 + container.turnEnergy, 10);
        else container.energy2 = Math.min(container.energy2 + container.turnEnergy, 10);
        LOGGER.info("Player " + player() + " gained " + container.turnEnergy + " energy!");
        LOGGER.debug("                         (" + ((isP1() ? container.energy1 : container.energy2) - container.turnEnergy) + "->" + (isP1() ? container.energy1 : container.energy2) + ")");
        container.tryDrawCard(isP1());
        //copy power values to turn power (must be done here before events have a chance to fire)
        for (int i = 0; i < 6; i++) turnPower[i] = state[i].power;
        //send turn event to all your cards
        for (int i = slot(0); i < slot(3); i++) {
            trigger(BattleEvent.TURN, i);
        }
    }
    
    /** ends a turn */
    public boolean endTurn() {
        //have all your cards attack
        for (int i = slot(0); i < slot(3); i++) {
            if (items.get(i) != null) {
                int opposite = opposite(i);
                attack(opposite, i);
                if (items.get(opposite) != null) {
                    //retaliate
                    attack(i, opposite);
                }
            }
        }
        //kind of finalize turn power values but they may be changed in the kill/death events
        for (int i = 0; i < 6; i++) state[i].power = turnPower[i];
        //card killing
        int[] marked = new int[6];
        for (BattleAttack attack : savedAttacks) {
            int target = attack.target;
            int source = attack.source;
            if (state[target].power < 0) {
                if (!trigger(BattleEvent.KILL, source, target, source)) continue;
                if (!trigger(BattleEvent.DEATH, target, target, source)) continue;
                LOGGER.info(items.get(source) + " killed " + items.get(target));
                removeCard(target);
            } else if (target == opposite(source) && state[target].power == 0 && state[source].power == 0) {
                //if two cards attacking each other hit 0 power, they should count as killing each other
                //1 = first attack, 2+ = second attack. Used to remember attack order
                if (marked[target] > 0) marked[source] = 2;
                else marked[source] = 1;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (marked[i] > 0 && marked[i+3] > 0) {
                //if i had first attack, source = i, else source = i+3, target is the other one
                int target = marked[i] == 1 ? i + 3 : i;
                int source = marked[i] == 1 ? i : i + 3;
                //debug thing to print who hits first
//                System.out.println("Player " + player(marked[i] == 1 ? getOwner(i) : getOwner(i + 3)) + " hit first");
                
                //post kill event for source (source -> target)
                boolean killTarget = trigger(BattleEvent.KILL, source, target, source);
                //dont kill target if it lives
                killTarget &= turnPower[target] <= 0;
                //post death event for target (source -> target)
                //java execution rules means this only executes if killTarget is true, this is intended.
                killTarget = killTarget && trigger(BattleEvent.DEATH, target, target, source);
                
                //post kill event for target (target -> source)
                boolean killSource = trigger(BattleEvent.KILL, target, source, target);
                //dont kill target if it lives
                killSource &= turnPower[source] <= 0;
                //post death event for source (target -> source)
                //java execution rules means this only executes if killSource is true, this is intended.
                killSource = killSource && trigger(BattleEvent.DEATH, source, source, target);
                
                if (killTarget) {
                    LOGGER.info(items.get(source) + " killed " + items.get(target));
                    removeCard(target);
                }
                if (killSource) {
                    LOGGER.info(items.get(target) + " killed " + items.get(source));
                    removeCard(source);
                }
            }
        }
        //reset saved attacks, no longer needed
        savedAttacks = new ArrayList<>();
        //finalize power values again
        for (int i = 0; i < 6; i++) state[i].power = turnPower[i];
        //player health
        if (container.health1 <= 0) {
            container.endGame(false);
            gameEnded = true;
            return false;
        } else if (container.health2 <= 0) {
            container.endGame(true);
            gameEnded = true;
            return false;
        }
        //game continues, return true
        return true;
    }
    
    /** initiates a turn switch and starts the next turn */
    public void nextTurn() {
        //switch turns
        container.isPlayer1Turn = !container.isPlayer1Turn;
        //increase energy per turn (max 5)
        if (container.turnEnergy < 5) ++container.turnEnergy;
        //increase regular turn
        ++container.turn;
        //start the next turn
        startTurn();
    }
    
    /** performs an attack. please ensure ahead of time the source card is valid */
    public void attack(int target, int source) {
        if (items.get(target) == null) {
            if (getOwner(target)) container.health1 -= state[source].power;
            else container.health2 -= state[source].power;
            LOGGER.info(items.get(source) + " dealt " + state[source].power + " damage to Player " + player(!isP1()));
        } else {
            if (!trigger(BattleEvent.FIGHT, source, target, source, BattleEvent.Distribution.COLUMN)) return;
            if (!trigger(BattleEvent.DAMAGED, target, target, source, BattleEvent.Distribution.COLUMN)) return;
            turnPower[target] -= state[source].power;
            LOGGER.info(items.get(source) + " dealt " + state[source].power + " damage to " + items.get(target));
            savedAttacks.add(new BattleAttack(target, source));
        }
        return;
    }

    public void directAttack(int target, int source, int amount) {
        directAttack(target, source, amount, true, true);
    }

    /** performs an attack dealing a specific amount of damage */
    public void directAttack(int target, int source, int amount, boolean doAttackTrigger, boolean doDamageTrigger) {
        if (items.get(target) == null) {
            if (getOwner(target)) container.health1 -= amount;
            else container.health2 -= amount;
            LOGGER.info(items.get(source) + " dealt " + amount + " damage to Player " + player(!isP1()));
        } else {
            if(doAttackTrigger) if (!trigger(BattleEvent.FIGHT, source, target, source, BattleEvent.Distribution.COLUMN)) return;
            if(doDamageTrigger) if (!trigger(BattleEvent.DAMAGED, target, target, source, BattleEvent.Distribution.COLUMN)) return;
            turnPower[target] -= amount;
            LOGGER.info(items.get(source) + " dealt " + amount + " damage to " + items.get(target));
            savedAttacks.add(new BattleAttack(target, source));
        }
        return;
    }
    
    /*
     * Events/Triggers
     * slot - The card slot the event is firing on (the one with the ability)
     * target - The target slot for the event
     * source - The source slot for the event
     * 
     * If any trigger returns false, the event is cancelled immediately
     * 
     * condition in the first trigger method is for doing checks on whether to actually fire an event or not and maybe doing some logging before it
     */
    
    public boolean trigger(BattleEvent event, int slot, int target, int source, BattleEvent.Distribution targets, Function<Integer, Boolean> condition) {
        int[] slots = targets.apply(slot, this);
        for (int i = 0; i < slots.length; i++) {
            if (condition.apply(slots[i]) && !trigger(event, slots[i], target, source)) return false;
        }
        return true;
    }
    
    public boolean trigger(BattleEvent event, int slot, int target, int source, BattleEvent.Distribution targets) {
        int[] slots = targets.apply(slot, this);
        for (int i = 0; i < slots.length; i++) {
            if (!trigger(event, slots[i], target, source)) return false;
        }
        return true;
    }
    
    public boolean trigger(BattleEvent event, int slot, int target, int source) {
        BuddycardItem card = items.get(slot);
        if (card != null && card.getAbilities().containsKey(event)) {
            for (BattleAbility ability : card.getAbilities().get(event)) {
                if (!ability.ability.trigger(this, slot, target, source)) return false;
            }
        }
        return true;
    }
    
    public boolean trigger(BattleEvent event, int slot) {
        return trigger(event, slot, slot, slot);
    }
    
    public boolean playCard(int slot, ItemStack stack, boolean p1) {
        if (!(stack.getItem() instanceof BuddycardItem item)) return false;
        int cost = item.getCost(stack);
        if (container.energy(p1) < cost) return false;
        if (p1) container.energy1 -= cost;
        else container.energy2 -= cost;
        return addCard(slot, stack, item);
    }
    
    /** adds a card into play */
    public boolean addCard(int slot, ItemStack stack, BuddycardItem item) {
        if (items.get(slot) != null) return false;
        //triggers when cards are manually placed anyways
        // container.setItem(translateFrom(slot), stack);
        items.set(slot, item);
        state[slot] = new BattleCardState(item.getPower(stack));
        turnPower[slot] = item.getPower(stack);
        trigger(BattleEvent.PLAYED, slot);
        return true;
    }
    
    /** moves a card if you need that for whatever reason */
    public BuddycardItem moveCard(int target, int destination) {
        if (items.get(destination) == null) {
            BuddycardItem item = items.set(target, null);
            if (item != null) {
                items.set(destination, item);
                state[destination] = state[target];
                state[target] = new BattleCardState(0);
                container.setItem(translateFrom(destination), container.removeItem(translateFrom(target), 1));
            }
            return item;
        }
        return null;
    }
    
    /** removes a card from play. NO DAMAGE/DEATH EVENTS ARE FIRED */
    public BuddycardItem removeCard(int target) {
        if (items.get(target) != null) {
            container.returnToDeck(getOwner(target), translateFrom(target));
            turnPower[target] = 0;
            state[target].power = 0;
            return items.set(target, null);
        }
        return null;
    }
    
    public BuddycardItem getCard(int slot) {
        return items.get(slot);
    }
    
    public boolean canPlay(boolean p1, BuddycardItem card, ItemStack stack) {
        return p1 == isP1() && container.energy(p1) >= card.getCost(stack);
    }
    
    public boolean isP1() {
        return container.isPlayer1Turn;
    }
    
    public char player() {
        return player(isP1());
    }
    
    public static char player(boolean p1) {
        return p1 ? '1' : '2';
    }
    
    /** Automatic slot calculation based off of current turn<br>
     *  Only pass 0-2 */
    public int slot(int slot) {
        return slot(slot, isP1());
    }
    
    static public int slot(int slot, boolean p1) {
        return p1 ? slot : slot + 3;
    }
    
    static public boolean isOpposite(int slot, int opposite) {
        return slot % 3 == opposite % 3;
    }
    
    static public int opposite(int slot) {
        return slot < 3 ? slot + 3 : slot - 3;
    }
    
    /** Adjacent slots L->R */
    static public int[] adjacent(int slot) {
        return switch(slot) {
        default -> new int[] {-1};
        case 0 -> new int[] {1};  //X1____
        case 1 -> new int[] {0,2};//0X2___
        case 2 -> new int[] {1};  //_1X___
        case 3 -> new int[] {4};  //___X4_
        case 4 -> new int[] {3,5};//___3X5
        case 5 -> new int[] {4};  //____4X
        };
    }
    
    /** Opposite first, L->R adjacent next */
    static public int[] allAdjacent(int slot) {
        return switch(slot) {
        default -> new int[] {-1};
        case 0 -> new int[] {4,1};  //X1_4__
        case 1 -> new int[] {5,0,2};//0X2_5_
        case 2 -> new int[] {6,1};  //_1X__6
        case 3 -> new int[] {0,4};  //0__X4_
        case 4 -> new int[] {1,3,5};//_1_3X5
        case 5 -> new int[] {2,4};  //__2_4X
        };
    };
    
    /** true = p1, false = p2 */
    static public boolean getOwner(int slot) {
        return switch(slot) {
        default -> true;
        case 0,1,2 -> true;
        case 3,4,5 -> false;
        };
    }
    
    static public int translateTo(int slot) {
        return switch(slot) {
        default -> debugError("Invalid slot translateTo: " + slot);
        case 4 -> 0;
        case 5 -> 1;
        case 6 -> 2;
        case 11 -> 3;
        case 12 -> 4;
        case 13 -> 5;
        };
    }
    
    static public int translateFrom(int slot) {
        return switch(slot) {
        default -> debugError("Invalid slot translateFrom: " + slot);
        case 0 -> 4;
        case 1 -> 5;
        case 2 -> 6;
        case 3 -> 11;
        case 4 -> 12;
        case 5 -> 13;
        };
    }
    
    static private int debugError(String errmsg) {
        LOGGER.debug(errmsg);
        return -1;
    }
    
    private static final class BattleAttack {
        public final int target;
        public final int source;
        public BattleAttack(int target, int source) {
            this.target = target;
            this.source = source;
        }
    }
    
}