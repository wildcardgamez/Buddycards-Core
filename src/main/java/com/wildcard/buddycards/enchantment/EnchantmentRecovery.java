package com.wildcard.buddycards.enchantment;

import com.wildcard.buddycards.item.BuddysteelSetMedalItem;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentRecovery extends Enchantment{
    public EnchantmentRecovery() {
        super(Enchantment.Rarity.RARE, COOLDOWN_ITEMS, EquipmentSlot.values());
    }

    public int getMinCost(int par1) {
        return 15 * (par1 - 1);
    }

    public int getMaxCost(int par1) {
        return 25 + (par1 * 10);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    static final EnchantmentCategory COOLDOWN_ITEMS = EnchantmentCategory.create("COOLDOWN_ITEMS", i -> (i.equals(BuddycardsItems.LUMINIS_HELMET.get()) || i.equals(BuddycardsItems.ZYLEX_BOOTS.get())));
}
