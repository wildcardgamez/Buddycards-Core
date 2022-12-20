package com.wildcard.buddycards.container;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.item.DeckboxItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class BattleContainer extends SimpleContainer {
    static final String LOG = "battlesLog." + Buddycards.MOD_ID + ".";
    public boolean isPlayer1Turn = false;
    public DeckboxContainer deck1, deck2;
    public Component name1, name2;
    public final ArrayList<MutableComponent> battleLog = new ArrayList<>();

    public BattleContainer() {
        super(14);
    }

    public void startGame() {
        if(Math.random() < .5)
            isPlayer1Turn = true;
        name1 = getItem(0).getDisplayName().plainCopy();
        name2 = getItem(7).getDisplayName().plainCopy();
        battleLogWithName("go_first");
        deck1 = new DeckboxContainer(getItem(0));
        deck2 = new DeckboxContainer(getItem(7));
        deck1.startOpen();
        deck2.startOpen();
        setChanged();
        tryDrawCard(true);
        tryDrawCard(true);
        tryDrawCard(false);
        tryDrawCard(false);
        battleLog("starting_draw");
        tryDrawCard(isPlayer1Turn);
        battleLogWithName("turn_draw");
        setChanged();
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

    public void battleLog(String name) {
        battleLog.add(new TranslatableComponent(LOG + name));
    }

    public void battleLogWithName(String name) {
        battleLog.add(new TextComponent("").append(isPlayer1Turn ? name1 : name2).append(new TranslatableComponent(LOG + name)));
    }
}
