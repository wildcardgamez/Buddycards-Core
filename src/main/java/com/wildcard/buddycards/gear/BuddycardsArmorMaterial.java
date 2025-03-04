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
    BUDDYSTEEL(12, 36, new int[]{3, 6, 8, 3}, 1, 0, BuddycardsItems.BUDDYSTEEL_INGOT, "buddysteel"),
    LUMINIS(8, 18, new int[]{3, 6, 8, 3}, 1, 0, BuddycardsItems.CRIMSON_LUMINIS, "luminis"),
    ZYLEX(8, 18, new int[]{3, 6, 8, 3}, 1, 0, BuddycardsItems.ZYLEX, "zylex"),
    CHARGED_BUDDYSTEEL(13, 38, new int[]{3, 6, 8, 3}, 2, 0.1F, BuddycardsItems.CHARGED_BUDDYSTEEL_INGOT, "charged_buddysteel"),
    PERFECT_BUDDYSTEEL(15, 45, new int[]{4, 7, 9, 4}, 2, 0.15F, BuddycardsItems.PERFECT_BUDDYSTEEL_INGOT, "perfect_buddysteel");

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
