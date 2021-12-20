package com.wildcard.buddycards.item;

import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.world.item.Rarity;

import java.util.Random;

public class MysteryBuddycardPackItem extends BuddycardPackItem{
    public MysteryBuddycardPackItem(BuddycardsItems.BuddycardRequirement shouldLoad, int amount, int foils, boolean includeUnloaded, Properties properties) {
        super(shouldLoad, "mystery", amount, foils, properties);
        INCLUDE_UNLOADED = includeUnloaded;
    }

    final boolean INCLUDE_UNLOADED;

    @Override
    public BuddycardItem rollCard(Random random) {
        float rand = random.nextFloat();
        Rarity rarity;
        if (rand < .6)
            rarity = Rarity.COMMON;
        else if (rand < .9)
            rarity = Rarity.UNCOMMON;
        else if (rand < .975)
            rarity = Rarity.RARE;
        else
            rarity = Rarity.EPIC;
        BuddycardItem card = BuddycardItem.CARDS.get((int) (random.nextFloat() *  BuddycardItem.CARDS.size()));
        while (card.getRarity() != rarity || !card.shouldBeInMysteryPacks()) {
            card =  BuddycardItem.CARDS.get((int) (random.nextFloat() *  BuddycardItem.CARDS.size()));
        }
        return card;
    }
}
