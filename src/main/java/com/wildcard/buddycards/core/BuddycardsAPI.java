package com.wildcard.buddycards.core;

import com.wildcard.buddycards.item.BuddycardItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BuddycardsAPI {
    private static final List<BuddycardItem> CARDS = new ArrayList<>();
    private static final List<BuddycardSet> SETS = new ArrayList<>();

    public static void registerCard(BuddycardItem card) {
        CARDS.add(card);
        card.getSet().addCard(card);
    }

    public static void registerSet(BuddycardSet set) {
        for (BuddycardSet cardSet : SETS) {
            if (cardSet.getName().equalsIgnoreCase(set.getName())) {
                throw new IllegalArgumentException("Set '" + set.getName() + "' already exists");
            }
        }
        SETS.add(set);
    }

    public static Collection<BuddycardItem> getAllCards() {
        return Collections.unmodifiableCollection(CARDS);
    }

    public static Collection<BuddycardSet> getAllSets() {
        return Collections.unmodifiableCollection(SETS);
    }

    public static BuddycardItem findCard(BuddycardSet set, int cardNumber) {
        for (BuddycardItem card : set.cards)
            if(card.getCardNumber() == cardNumber)
                return card;
        return null;
    }

    public static BuddycardSet findSet(String setName) {
        for (BuddycardSet set : SETS)
            if(set.getName().equals(setName))
                return set;
        return null;
    }

    public static BuddycardSet getSetById(int set) {
        return SETS.get(set);
    }

    public static BuddycardItem getCardById(int set, int num) {
        return SETS.get(set).getCardById(num);
    }
}
