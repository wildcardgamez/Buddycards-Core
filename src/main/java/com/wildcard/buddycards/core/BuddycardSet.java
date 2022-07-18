package com.wildcard.buddycards.core;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.item.BuddysteelSetMedalItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class BuddycardSet {

    protected final List<BuddycardItem> cards = new ArrayList<>();
    protected final String name;

    @Nullable
    protected Supplier<BuddysteelSetMedalItem> medalSupplier;

    @Nullable
    protected String descriptionId;

    public BuddycardSet(String name) {
        this.name = name;
        BuddycardsAPI.registerSet(this);
    }

    public String getName() {
        return name;
    }

    public String getDescriptionId() {
        if (descriptionId == null) {
            descriptionId = "item." + Buddycards.MOD_ID + ".buddycard.set_" + getName();
        }
        return descriptionId;
    }

    public void setMedal(Supplier<BuddysteelSetMedalItem> supplier) {
        this.medalSupplier = supplier;
    }

    @Nullable
    public BuddysteelSetMedalItem getMedal() {
        if (medalSupplier == null) {
            return null;
        }
        return medalSupplier.get();
    }

    public void addCard(BuddycardItem card) {
        cards.add(card);
    }

    public Collection<BuddycardItem> getCards() {
        return Collections.unmodifiableCollection(cards);
    }
}
