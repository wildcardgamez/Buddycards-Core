package com.wildcard.buddycards.core;

import com.wildcard.buddycards.registries.BuddycardsComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public interface ICollectionTieredItem {
    default int getTier(ItemStack stack) {
        int tier = stack.get(BuddycardsComponents.COLLECTION_TIER);
        if (tier > 1)
            tier--;
        return tier;
    }

    default int getExactTier(ItemStack stack) {
        return stack.get(BuddycardsComponents.COLLECTION_TIER);
    }

    default Component getTierComponent(ItemStack stack) {
        return Component.translatable("gui.buddycards.tier_component." + getTier(stack)).withStyle(ChatFormatting.GOLD);
    }
}
