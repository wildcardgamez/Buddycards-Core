package com.wildcard.buddycards.enchantment;

import com.wildcard.buddycards.item.BuddysteelSetMedalItem;
import com.wildcard.buddycards.item.LuminisSetMedalItem;
import com.wildcard.buddycards.item.ZylexSetMedalItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentBuddyBoost extends Enchantment {
    public EnchantmentBuddyBoost() {
        super(Rarity.RARE, BUDDYSTEEL_MEDAL, EquipmentSlot.values());
    }

    @Override
    public int getMinCost(int par1) {
        return 15 * (par1 - 1);
    }

    @Override
    public int getMaxCost(int par1) {
        return 25 + (par1 * 10);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    static final EnchantmentCategory BUDDYSTEEL_MEDAL = EnchantmentCategory.create("BUDDYSTEEL_MEDAL", i -> (i instanceof BuddysteelSetMedalItem) || (i instanceof LuminisSetMedalItem) || (i instanceof ZylexSetMedalItem));
}
