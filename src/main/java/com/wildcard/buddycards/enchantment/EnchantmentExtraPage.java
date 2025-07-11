package com.wildcard.buddycards.enchantment;

import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentExtraPage extends Enchantment {
    public EnchantmentExtraPage() {
        super(Rarity.RARE, BuddycardsMisc.BUDDYCARD_BINDER, EquipmentSlot.values());
    }

    @Override
    public int getMinCost(int par1) {
        return 6 * (par1 - 1);
    }

    @Override
    public int getMaxCost(int par1) {
        return 6 + (par1 * 6);
    }

    @Override
    public int getMaxLevel() {
        return 7;
    }
}
