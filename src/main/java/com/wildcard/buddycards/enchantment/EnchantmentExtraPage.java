package com.wildcard.buddycards.enchantment;

import com.wildcard.buddycards.item.BuddycardBinderItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentExtraPage extends Enchantment {
    public EnchantmentExtraPage() {
        super(Rarity.RARE, BUDDY_BINDER, EquipmentSlot.values());
    }

    @Override
    public int getMinCost(int par1) {
        return 15 * (par1 - 1);
    }

    @Override
    public int getMaxCost(int par1) {
        return 20 + (par1 * 10);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    static final EnchantmentCategory BUDDY_BINDER = EnchantmentCategory.create("BUDDY_BINDER", BuddycardBinderItem.class::isInstance);
}
