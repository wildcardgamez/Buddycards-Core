package com.wildcard.buddycards.container;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.battles.BattleComponent;
import com.wildcard.buddycards.battles.TextureBattleIcon;
import com.wildcard.buddycards.battles.game.BattleGame;
import com.wildcard.buddycards.battles.game.BuddycardFilter;
import com.wildcard.buddycards.block.entity.PlaymatBlockEntity;
import com.wildcard.buddycards.item.DeckboxItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleContainer extends SimpleContainer {
    static final String LOG = "battles.log." + Buddycards.MOD_ID + ".";
    public static final Random random = new Random();
    public boolean isPlayer1Turn = false;
    public PlaymatBlockEntity entity;
    public DeckboxContainer deck1, deck2;
    public Component name1, name2;

    public int health1, health2;
    public int energy1, energy2;
    public int turnEnergy;
    public int turn;
    public BattleGame game;
    
    public final List<BattleComponent> battleLog = new ArrayList<>();

    public BattleContainer() {
        super(14);
        game = new BattleGame(this);
    }

    public void startGame() {
        if(Math.random() < .5)
            isPlayer1Turn = true;
        name1 = getItem(0).getDisplayName().plainCopy();
        name2 = getItem(7).getDisplayName().plainCopy();
        addLog(new BattleComponent(Component.literal("").append(isPlayer1Turn ? name1 : name2).append(Component.translatable("battles.log.buddycards.go_first")), List.of(TextureBattleIcon.playIcon)));
        deck1 = new DeckboxContainer(getItem(0));
        deck2 = new DeckboxContainer(getItem(7));
        deck1.startOpen();
        deck2.startOpen();
        health1 = health2 = 20;
        energy1 = energy2 = 0;
        turnEnergy = 1;
        turn = 1;
        game = new BattleGame(this);
        game.beginGame();
        entity.setChanged();
    }

    public void reload() {
        name1 = getItem(0).getDisplayName();
        name2 = getItem(7).getDisplayName();
    }

    public boolean tryDrawCard(boolean p1) {
        //If there is no deckbox, fail
        if(!(getItem(p1 ? 0 : 7).getItem() instanceof DeckboxItem))
            return false;
        //If no cards are in the box, fail
        if((p1 && deck1.isEmpty()) || (!p1 && deck2.isEmpty()))
            return false;
        //Check the hand slots
        for (int i = p1 ? 1 : 8; i < (p1 ? 4 : 11); i++) {
            //Once it finds an empty one, draw a random card from the deckbox and return success
            if(getItem(i).isEmpty()) {
                ItemStack card = p1 ? deck1.removeItem(random.nextInt(16), 1) : deck2.removeItem(random.nextInt(16), 1);
                while(card.isEmpty())
                    card = p1 ? deck1.removeItem(random.nextInt(16), 1) : deck2.removeItem(random.nextInt(16), 1);
                setItem(i, card);
                if(p1)
                    deck1.setChanged();
                else
                    deck2.setChanged();
                setChanged();
                return true;
            }
        }
        //If the hand was full, return false
        return false;
    }

    public boolean tryPutInHand(boolean p1, ItemStack card) {
        for (int i = p1 ? 1 : 8; i < (p1 ? 4 : 11); i++) {
            if(getItem(i).isEmpty()) {
                setItem(i, card);
                setChanged();
                return true;
            }
        }
        return false;
    }

    public boolean tryTutorCard(boolean p1, BuddycardFilter filter) {
        //If there is no deckbox, fail
        if(!(getItem(p1 ? 0 : 7).getItem() instanceof DeckboxItem))
            return false;
        //If no cards are in the box, fail
        if((p1 && deck1.isEmpty()) || (!p1 && deck2.isEmpty()))
            return false;
        //Check the hand slots
        for (int i = p1 ? 1 : 8; i < (p1 ? 4 : 11); i++) {
            //Once it finds an empty one, get a random slot, then increment through the other slots until it finds a valid card to put in hand or has checked every card
            if(getItem(i).isEmpty()) {
                int j = random.nextInt(16);
                int initialSlot = j;
                ItemStack card = p1 ? deck1.removeItem((++j)%16, 1) : deck2.removeItem((++j)%16, 1);
                while(initialSlot != j && (card.isEmpty() || !filter.filter(card)))
                    card = p1 ? deck1.removeItem((++j)%16, 1) : deck2.removeItem((++j)%16, 1);
                setItem(i, card);
                if(p1)
                    deck1.setChanged();
                else
                    deck2.setChanged();
                setChanged();
                return true;
            }
        }
        //If the hand was full, return false
        return false;
    }
    
    public boolean returnToDeck(boolean p1, int slot) {
        ItemStack stack = removeItem(slot, 1);
        if (p1) return deck1.addItem(stack).isEmpty();
        else return deck2.addItem(stack).isEmpty();
    }
    
    public void endGame(boolean p1Victory) {
        System.out.println("Player " + BattleGame.player(p1Victory) + " Wins!");
        addLog(new BattleComponent(Component.literal("").append(p1Victory ? name1 : name2).append(Component.translatable("battles.log.buddycards.victory")), List.of(TextureBattleIcon.spacerIcon, TextureBattleIcon.winIcon, TextureBattleIcon.spacerIcon)));
        deck1.saveStats(p1Victory);
        deck2.saveStats(!p1Victory);
        this.syncData();
    }
    
    public int energy(boolean p1) {
        return p1 ? energy1 : energy2;
    }

    public void spendEnergy(boolean p1, int amt) {
        if(p1) energy1 -= amt;
        else energy2 -= amt;
    }

    public void addLog(BattleComponent log) {
        battleLog.add(0, log);
    }

    public void syncData() {
        if (this.entity != null && this.entity.getLevel() instanceof ServerLevel level) {
            level.sendBlockUpdated(entity.getBlockPos(), entity.getBlockState(), entity.getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        }
    }
}
