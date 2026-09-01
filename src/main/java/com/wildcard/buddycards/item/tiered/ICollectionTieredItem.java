package com.wildcard.buddycards.item.tiered;

import com.wildcard.buddycards.registries.BuddycardsComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public interface ICollectionTieredItem {
    default int getCollectionTier(ItemStack stack) {
        int tier = stack.get(BuddycardsComponents.COLLECTION_TIER);
        if (tier > 1)
            tier--;
        return tier;
    }

    default int getExactCollectionTier(ItemStack stack) {
        return stack.get(BuddycardsComponents.COLLECTION_TIER);
    }

    default Component getCollectionTierComponent(ItemStack stack) {
        return Component.translatable("gui.buddycards.tier_component." + getCollectionTier(stack)).withStyle(ChatFormatting.GOLD);
    }

    interface ExtraAttributes {
        void applyAttributes(ItemAttributeModifiers.Builder builder, int tier, EquipmentSlotGroup slot);
    }
}
