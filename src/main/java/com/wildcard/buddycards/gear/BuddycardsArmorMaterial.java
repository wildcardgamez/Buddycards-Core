package com.wildcard.buddycards.gear;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum BuddycardsArmorMaterial implements ArmorMaterial {
    BUDDYSTEEL(12, 36, new int[]{3, 8, 6, 3}, 1, 0, BuddycardsItems.BUDDYSTEEL_INGOT, "buddysteel"),
    LUMINIS(8, 18, new int[]{3, 8, 6, 3}, 1, 0, BuddycardsItems.CRIMSON_LUMINIS, "luminis"),
    ZYLEX(8, 18, new int[]{3, 8, 6, 3}, 1, 0, BuddycardsItems.ZYLEX, "zylex"),
    CHARGED_BUDDYSTEEL(13, 38, new int[]{3, 8, 6, 3}, 2, 0.1F, BuddycardsItems.CHARGED_BUDDYSTEEL_INGOT, "charged_buddysteel"),
    CRIMSON_BUDDYSTEEL(15, 42, new int[]{3, 8, 6, 3}, 2, 0.15F, BuddycardsItems.CRIMSON_BUDDYSTEEL_INGOT, "crimson_buddysteel"),
    VOID_BUDDYSTEEL(13, 38, new int[]{4, 9, 7, 4}, 2, 0.1F, BuddycardsItems.VOID_BUDDYSTEEL_INGOT, "void_buddysteel"),
    PERFECT_BUDDYSTEEL(15, 45, new int[]{4, 9, 7, 4}, 3, 0.15F, BuddycardsItems.PERFECT_BUDDYSTEEL_INGOT, "perfect_buddysteel"),
    TRUE_PERFECT_BUDDYSTEEL(16, 64, new int[]{5, 11, 9, 5}, 4, 0.2F, BuddycardsItems.TRUE_PERFECT_BUDDYSTEEL_INGOT, "true_perfect_buddysteel");

    BuddycardsArmorMaterial(int enchVal, int dura, int[] red, float toughness, float kbr, Supplier<Item> mat, String nameIn) {
        ench = enchVal;
        duraMult = dura;
        material = mat;
        name = nameIn;
        dam_red = red;
        this.toughness = toughness;
        this.kbr = kbr;
    }
    int ench;
    int duraMult;
    Supplier<Item> material;
    String name;
    int[] dam_red;
    float toughness;
    float kbr;

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return HEALTH_PER_SLOT[type.ordinal()] * duraMult;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return dam_red[type.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return ench;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_DIAMOND;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(material.get());
    }

    @Override
    public String getName() {
        return Buddycards.MOD_ID + ":" + name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return kbr;
    }
}
