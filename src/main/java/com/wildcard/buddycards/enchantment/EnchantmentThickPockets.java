package com.wildcard.buddycards.enchantment;

import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentThickPockets extends Enchantment {
    public EnchantmentThickPockets() {
        super(Rarity.RARE, BuddycardsMisc.BUDDYCARD_BINDER, EquipmentSlot.values());
    }

    @Override
    public int getMinCost(int par1) {
        return 10 + 10 * (par1 - 1);
    }

    @Override
    public int getMaxCost(int par1) {
        return 20 + (par1 * 10);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
