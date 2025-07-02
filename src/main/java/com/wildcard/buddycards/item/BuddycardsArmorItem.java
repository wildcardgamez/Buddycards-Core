package com.wildcard.buddycards.item;

import com.wildcard.buddycards.gear.BuddycardsArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import static com.wildcard.buddycards.registries.BuddycardsItems.UNCOMMON_TOOL_PROPERTIES;

public class BuddycardsArmorItem extends ArmorItem {
    public BuddycardsArmorItem(ArmorMaterial materialIn, Type type) {
        super(materialIn, type, UNCOMMON_TOOL_PROPERTIES);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        if (material.equals(BuddycardsArmorMaterial.BUDDYSTEEL)) return Rarity.COMMON;
        if (material.equals(BuddycardsArmorMaterial.CHARGED_BUDDYSTEEL)) return Rarity.UNCOMMON;
        if (material.equals(BuddycardsArmorMaterial.TRUE_PERFECT_BUDDYSTEEL)) return Rarity.EPIC;
        return Rarity.RARE;
    }
}
