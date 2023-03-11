package com.wildcard.buddycards.container;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.battles.BattleComponent;
import com.wildcard.buddycards.block.entity.PlaymatBlockEntity;
import com.wildcard.buddycards.item.DeckboxItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BattleContainer extends SimpleContainer {
    static final String LOG = "battlesLog." + Buddycards.MOD_ID + ".";
    public boolean isPlayer1Turn = false;
    public PlaymatBlockEntity entity;
    public DeckboxContainer deck1, deck2;
    public Component name1, name2;

    public int health1, health2;
    public int energy1, energy2;
    public final List<BattleComponent> battleLog = new ArrayList<>();

    public BattleContainer() {
        super(14);
    }

    public void startGame() {
        if(Math.random() < .5)
            isPlayer1Turn = true;
        name1 = getItem(0).getDisplayName().plainCopy();
        name2 = getItem(7).getDisplayName().plainCopy();

        health1 = health2 = 20;
        energy1 = energy2 = 0;
        addLogWithName("go_first");
        deck1 = new DeckboxContainer(getItem(0));
        deck2 = new DeckboxContainer(getItem(7));
        deck1.startOpen();
        deck2.startOpen();
        tryDrawCard(true);
        tryDrawCard(true);
        tryDrawCard(false);
        tryDrawCard(false);
        addLog("starting_draw");
        tryDrawCard(isPlayer1Turn);
        addLogWithName("turn_draw");
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
        for (int i = p1 ? 1 : 8; i < (p1 ? 4 : 10); i++) {
            //Once it finds an empty one, draw a random card from the deckbox and return success
            if(getItem(i).isEmpty()) {
                ItemStack card = p1 ? deck1.removeItem((int)(Math.random() * 16), 1) : deck2.removeItem((int)(Math.random() * 16), 1);
                while(card.isEmpty())
                    card = p1 ? deck1.removeItem((int)(Math.random() * 16), 1) : deck2.removeItem((int)(Math.random() * 16), 1);
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

    public void addLog(String name) {
        battleLog.add(new BattleComponent(new TranslatableComponent(LOG + name)));
    }
    public void addLogWithName(String name) {
        battleLog.add(new BattleComponent(new TextComponent("").append(isPlayer1Turn ? name1 : name2).append(new TranslatableComponent(LOG + name))));
    }
}
