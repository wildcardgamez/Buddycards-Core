package com.wildcard.buddycards.battles.game;

import java.util.function.BiFunction;

import com.wildcard.buddycards.battles.game.BattleAbility.BattleAbilityFunc;

public class BattleEvent {
    
    public final String name;
    
    public BattleEvent(String name) {
        this.name = name;
    }
    
    public BattleAbility ability(String name, BattleAbilityFunc func) {
        return new BattleAbility(this, name, func);
    }
    
    /** When a cad is played */
    public static final BattleEvent PLAYED = new BattleEvent("played");
    /** At the beginning of every turn, before cards are played */
    public static final BattleEvent TURN = new BattleEvent("turn");
    /** When a card attacks or is attacked */
    public static final BattleEvent FIGHT = new BattleEvent("fight"); //cards fight
    /** When a card kills another */
    public static final BattleEvent KILL = new BattleEvent("kill");
    /** When a card dies */
    public static final BattleEvent DEATH = new BattleEvent("death");
    /** When any card dies */
    public static final BattleEvent OBSERVE_DEATH = new BattleEvent("observe_death");
    /** When a card loses power */
    public static final BattleEvent DAMAGED = new BattleEvent("damaged");
    /** When a card is redstone powered */
    public static final BattleEvent POWERED = new BattleEvent("powered");
    /** When a card is activated manually */
    public static final BattleEvent ACTIVATED = new BattleEvent("activated");
    
    
    
    public static interface Distribution extends BiFunction<Integer, BattleGame, int[]> {
        /** All cards */
        public static final Distribution ALL = (slot, game) -> new int[] {0,1,2,3,4,5};
        /** Adjacent cards INCLUDING enemy cards */
        public static final Distribution ALL_ADJACENT = (slot, game) -> game.allAdjacent(slot);
        /** Adjacent cards EXCLUDING enemy cards */
        public static final Distribution ADJACENT = (slot, game) -> game.adjacent(slot);
        /** All enemy cards */
        public static final Distribution ALL_ENEMY = (slot, game) -> slot < 3 ? new int[] {3,4,5} : new int[] {0,1,2};
        /** All of your cards */
        public static final Distribution ROW = (slot, game) -> slot < 3 ? new int[] {0,1,2} : new int[] {3,4,5};
        /** The provided card and the opposing card */
        public static final Distribution COLUMN = (slot, game) -> new int[] {slot, game.opposite(slot)};
        /** Only the opposing card */
        public static final Distribution OPPOSITE = (slot, game) -> new int[] {game.opposite(slot)};
        /** Only the provided card */
        public static final Distribution THIS = (slot, game) -> new int[] {slot};
    }
    
}