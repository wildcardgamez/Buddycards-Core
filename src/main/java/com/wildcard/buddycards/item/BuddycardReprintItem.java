package com.wildcard.buddycards.item;

import com.google.common.collect.ListMultimap;
import com.wildcard.buddycards.battles.game.BattleAbility;
import com.wildcard.buddycards.battles.game.BattleEvent;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.world.item.Rarity;

public class BuddycardReprintItem extends BuddycardItem{
    public BuddycardReprintItem(BuddycardsItems.BuddycardRequirement shouldLoad, BuddycardSet set, int cardNumber, Rarity rarity, Properties properties, int cost, int power, ListMultimap<BattleEvent, BattleAbility> abilities, BuddycardSet originalSet, int originalNum) {
        super(shouldLoad, set, cardNumber, rarity, properties, cost, power, abilities);
        ORIGINAL = BuddycardsAPI.findCard(originalSet, originalNum);
    }

    protected BuddycardItem ORIGINAL;

    @Override
    public BuddycardItem getOriginal() {
        return ORIGINAL;
    }
}
