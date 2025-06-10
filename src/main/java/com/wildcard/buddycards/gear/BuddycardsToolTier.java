package com.wildcard.buddycards.gear;

import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum BuddycardsToolTier implements Tier {
    BUDDYSTEEL(2048, 9.0F, 2.5F, 3, 12, BuddycardsItems.BUDDYSTEEL_INGOT),
    LUMINIS(1256, 9.0F, 3.0F, 3, 9, BuddycardsItems.CRIMSON_LUMINIS),
    ZYLEX(1674, 9.0F, 3.0F, 3, 9, BuddycardsItems.ZYLEX),
    CHARGED_BUDDYSTEEL(2560, 9.0F, 3.5F, 4, 13, BuddycardsItems.CHARGED_BUDDYSTEEL_INGOT),
    CRIMSON_BUDDYSTEEL(3072, 9.5F, 4.0F, 4, 15, BuddycardsItems.CRIMSON_BUDDYSTEEL_INGOT),
    VOID_BUDDYSTEEL(2560, 10.0F, 4.5F, 4, 13, BuddycardsItems.VOID_BUDDYSTEEL_INGOT),
    PERFECT_BUDDYSTEEL(4096, 10.0F, 4.5F, 5, 15, BuddycardsItems.PERFECT_BUDDYSTEEL_INGOT),
    TRUE_PERFECT_BUDDYSTEEL(6144, 12.0F, 6F, 6, 16, BuddycardsItems.TRUE_PERFECT_BUDDYSTEEL_INGOT);

    int uses;
    float speed;
    float dmg;
    int level;
    int ench;
    Supplier<Item> mat;

    BuddycardsToolTier(int uses, float speed, float dmg, int level, int ench, Supplier mat) {
        this.uses = uses;
        this.speed = speed;
        this.dmg = dmg;
        this.level = level;
        this.ench = ench;
        this.mat = mat;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return dmg;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getEnchantmentValue() {
        return ench;
    }
    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(mat.get());
    }
}