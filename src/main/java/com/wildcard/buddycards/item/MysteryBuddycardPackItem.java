package com.wildcard.buddycards.item;

import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class MysteryBuddycardPackItem extends BuddycardPackItem {
    public MysteryBuddycardPackItem(BuddycardsItems.BuddycardRequirement shouldLoad, int amount, int foils, SimpleWeightedRandomList<Rarity> rarityWeights, boolean includeUnloaded, Properties properties) {
        super(shouldLoad, "mystery", amount, foils, rarityWeights, properties);
        INCLUDE_UNLOADED = includeUnloaded;
    }

    protected final boolean INCLUDE_UNLOADED;

    @Override
    protected List<BuddycardItem> getPossibleCards(Rarity rarity) {
        return BuddycardItem.CARDS
                .stream()
                .filter(card -> card.getRarity() == rarity && card.shouldBeInMysteryPacks())
                .toList();
    }
}
