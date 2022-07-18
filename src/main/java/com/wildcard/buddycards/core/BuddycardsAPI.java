package com.wildcard.buddycards.core;

import com.wildcard.buddycards.item.BuddycardItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BuddycardsAPI {
    private static final List<BuddycardItem> CARDS = new ArrayList<>();
    private static final List<BuddycardSet> CARD_SETS = new ArrayList<>();

    public static void registerCard(BuddycardItem card) {
        CARDS.add(card);
        card.getSet().addCard(card);
    }

    public static void registerSet(BuddycardSet set) {
        for (BuddycardSet cardSet : CARD_SETS) {
            if (cardSet.getName().equalsIgnoreCase(set.getName())) {
                throw new IllegalArgumentException("Set '" + set.getName() + "' already exists");
            }
        }
        CARD_SETS.add(set);
    }

    public static Collection<BuddycardItem> getAllCards() {
        return Collections.unmodifiableCollection(CARDS);
    }

    public static Collection<BuddycardSet> getAllCardsets() {
        return Collections.unmodifiableCollection(CARD_SETS);
    }
}
