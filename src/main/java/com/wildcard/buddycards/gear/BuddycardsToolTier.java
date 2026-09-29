package com.wildcard.buddycards.gear;

import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public enum BuddycardsToolTier implements Tier {
    BUDDYSTEEL(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 2048, 8.0F, 2.5F, 3, 12, () -> Ingredient.of(BuddycardsItems.BUDDYSTEEL_INGOT)),
    LUMINIS(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1256, 9.0F, 3.0F, 3, 9, () -> Ingredient.of(BuddycardsItems.CRIMSON_LUMINIS)),
    ZYLEX(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1674, 9.0F, 3.0F, 3, 9, () -> Ingredient.of(BuddycardsItems.ZYLEX)),
    CHARGED_BUDDYSTEEL(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2560, 9.0F, 3.5F, 4, 14, () -> Ingredient.of(BuddycardsItems.CHARGED_BUDDYSTEEL_INGOT)),
    MIXED_BUDDYSTEEL(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 3072, 9.5F, 4.5F, 4, 15, () -> Ingredient.of(BuddycardsItems.CHARGED_BUDDYSTEEL_INGOT)),
    PERFECT_BUDDYSTEEL(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 4096, 10.0F, 5F, 5, 17, () -> Ingredient.of(BuddycardsItems.PERFECT_BUDDYSTEEL_INGOT)),
    TRUE_PERFECT_BUDDYSTEEL(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 6144, 11.0F, 5.5F, 6, 22, () -> Ingredient.of(BuddycardsItems.TRUE_PERFECT_BUDDYSTEEL_INGOT));

    TagKey<Block> incorrectBlockForDrops;
    int uses;
    float speed;
    float dmg;
    int level;
    int ench;
    Supplier<Ingredient> mat;

    BuddycardsToolTier(TagKey<Block> incorrectBlockForDrops, int uses, float speed, float dmg, int level, int ench, Supplier<Ingredient> mat) {
        this.incorrectBlockForDrops = incorrectBlockForDrops;
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
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlockForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return ench;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return mat.get();
    }
}