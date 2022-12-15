package com.wildcard.buddycards.container;

import com.wildcard.buddycards.item.DeckboxItem;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class PlaymatContainer extends SimpleContainer {
    public PlaymatContainer() {
        super(7);
    }

    public PlaymatContainer(PlaymatContainer opponent) {
        super(7);
        this.opponent = opponent;
    }

    public PlaymatContainer opponent;
    public DeckboxContainer deckbox;
    public final ArrayList<TranslatableComponent> battleLog = new ArrayList<>();

    public void setDeckbox(ItemStack deckbox) {
        setItem(0, deckbox);
        this.deckbox = new DeckboxContainer(deckbox);
        setChanged();
    }

    public boolean tryDrawCard() {
        //If there is no deckbox, fail
        if(!(getItem(0).getItem() instanceof DeckboxItem))
            return false;
        //If no cards are in the box, fail
        if(deckbox.isEmpty())
            return false;
        //Check the hand slots
        for (int i = 1; i < 4; i++) {
            //Once it finds an empty one, draw a random card from the deckbox and return success
            if(getItem(i).isEmpty()) {
                ItemStack card = deckbox.removeItem((int)(Math.random() * 16), 1);
                while(card.isEmpty())
                    card = deckbox.removeItem((int)(Math.random() * 16), 1);
                setItem(i, card);
                deckbox.setChanged();
                setChanged();
                return true;
            }
        }
        //If the hand was full, return false
        return false;
    }
}
