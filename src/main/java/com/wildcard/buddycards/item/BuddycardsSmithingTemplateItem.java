package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class BuddycardsSmithingTemplateItem extends SmithingTemplateItem {
    private static final Component CHARGED_BUDDYSTEEL_UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", new ResourceLocation(Buddycards.MOD_ID, "charged_buddysteel_upgrade"))).withStyle(ChatFormatting.GRAY);
    private static final Component CHARGED_BUDDYSTEEL_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.charged_buddysteel_upgrade.applies_to"))).withStyle(ChatFormatting.BLUE);
    private static final Component CHARGED_BUDDYSTEEL_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.charged_buddysteel_upgrade.ingredients"))).withStyle(ChatFormatting.BLUE);
    private static final Component CHARGED_BUDDYSTEEL_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.charged_buddysteel_upgrade.base_slot_description")));
    private static final Component CHARGED_BUDDYSTEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.charged_buddysteel_upgrade.additions_slot_description")));
    private static final Component CRIMSON_BUDDYSTEEL_UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", new ResourceLocation(Buddycards.MOD_ID, "crimson_buddysteel_upgrade"))).withStyle(ChatFormatting.GRAY);
    private static final Component CRIMSON_BUDDYSTEEL_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.crimson_buddysteel_upgrade.applies_to"))).withStyle(ChatFormatting.BLUE);
    private static final Component CRIMSON_BUDDYSTEEL_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.crimson_buddysteel_upgrade.ingredients"))).withStyle(ChatFormatting.BLUE);
    private static final Component CRIMSON_BUDDYSTEEL_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.crimson_buddysteel_upgrade.base_slot_description")));
    private static final Component CRIMSON_BUDDYSTEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.crimson_buddysteel_upgrade.additions_slot_description")));
    private static final Component VOID_BUDDYSTEEL_UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", new ResourceLocation(Buddycards.MOD_ID, "void_buddysteel_upgrade"))).withStyle(ChatFormatting.GRAY);
    private static final Component VOID_BUDDYSTEEL_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.void_buddysteel_upgrade.applies_to"))).withStyle(ChatFormatting.BLUE);
    private static final Component VOID_BUDDYSTEEL_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.void_buddysteel_upgrade.ingredients"))).withStyle(ChatFormatting.BLUE);
    private static final Component VOID_BUDDYSTEEL_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.void_buddysteel_upgrade.base_slot_description")));
    private static final Component VOID_BUDDYSTEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.void_buddysteel_upgrade.additions_slot_description")));
    private static final Component TRUE_PERFECT_BUDDYSTEEL_UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", new ResourceLocation(Buddycards.MOD_ID, "true_perfect_buddysteel_upgrade"))).withStyle(ChatFormatting.GRAY);
    private static final Component TRUE_PERFECT_BUDDYSTEEL_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.true_perfect_buddysteel_upgrade.applies_to"))).withStyle(ChatFormatting.BLUE);
    private static final Component TRUE_PERFECT_BUDDYSTEEL_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.true_perfect_buddysteel_upgrade.ingredients"))).withStyle(ChatFormatting.BLUE);
    private static final Component TRUE_PERFECT_BUDDYSTEEL_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.true_perfect_buddysteel_upgrade.base_slot_description")));
    private static final Component TRUE_PERFECT_BUDDYSTEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Buddycards.MOD_ID, "smithing_template.true_perfect_buddysteel_upgrade.additions_slot_description")));
    private static final ResourceLocation EMPTY_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
    private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
    private static final ResourceLocation EMPTY_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
    private static final ResourceLocation EMPTY_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
    private static final ResourceLocation EMPTY_SLOT_HOE = new ResourceLocation("item/empty_slot_hoe");
    private static final ResourceLocation EMPTY_SLOT_AXE = new ResourceLocation("item/empty_slot_axe");
    private static final ResourceLocation EMPTY_SLOT_SWORD = new ResourceLocation("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_SLOT_SHOVEL = new ResourceLocation("item/empty_slot_shovel");
    private static final ResourceLocation EMPTY_SLOT_PICKAXE = new ResourceLocation("item/empty_slot_pickaxe");
    private static final ResourceLocation EMPTY_SLOT_INGOT = new ResourceLocation("item/empty_slot_ingot");

    public BuddycardsSmithingTemplateItem(Component appliesTo, Component ingredients, Component upgradeDescription, Component baseSlotDescription, Component additionsSlotDescription, List<ResourceLocation> baseSlotEmptyIcons, List<ResourceLocation> additionalSlotEmptyIcons) {
        super(appliesTo, ingredients, upgradeDescription, baseSlotDescription, additionsSlotDescription, baseSlotEmptyIcons, additionalSlotEmptyIcons);
    }

    public static BuddycardsSmithingTemplateItem createChargedBuddysteelUpgradeTemplate() {
        return new BuddycardsSmithingTemplateItem(CHARGED_BUDDYSTEEL_UPGRADE_APPLIES_TO, CHARGED_BUDDYSTEEL_UPGRADE_INGREDIENTS, CHARGED_BUDDYSTEEL_UPGRADE, CHARGED_BUDDYSTEEL_UPGRADE_BASE_SLOT_DESCRIPTION, CHARGED_BUDDYSTEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, createUpgradeIconList(), createUpgradeMaterialList());
    }

    public static BuddycardsSmithingTemplateItem createCrimsonBuddysteelUpgradeTemplate() {
        return new BuddycardsSmithingTemplateItem(CRIMSON_BUDDYSTEEL_UPGRADE_APPLIES_TO, CRIMSON_BUDDYSTEEL_UPGRADE_INGREDIENTS, CRIMSON_BUDDYSTEEL_UPGRADE, CRIMSON_BUDDYSTEEL_UPGRADE_BASE_SLOT_DESCRIPTION, CRIMSON_BUDDYSTEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, createUpgradeIconList(), createUpgradeMaterialList());
    }

    public static BuddycardsSmithingTemplateItem createVoidBuddysteelUpgradeTemplate() {
        return new BuddycardsSmithingTemplateItem(VOID_BUDDYSTEEL_UPGRADE_APPLIES_TO, VOID_BUDDYSTEEL_UPGRADE_INGREDIENTS, VOID_BUDDYSTEEL_UPGRADE, VOID_BUDDYSTEEL_UPGRADE_BASE_SLOT_DESCRIPTION, VOID_BUDDYSTEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, createUpgradeIconList(), createUpgradeMaterialList());
    }

    public static BuddycardsSmithingTemplateItem createTruePerfectBuddysteelUpgradeTemplate() {
        return new BuddycardsSmithingTemplateItem(TRUE_PERFECT_BUDDYSTEEL_UPGRADE_APPLIES_TO, TRUE_PERFECT_BUDDYSTEEL_UPGRADE_INGREDIENTS, TRUE_PERFECT_BUDDYSTEEL_UPGRADE, TRUE_PERFECT_BUDDYSTEEL_UPGRADE_BASE_SLOT_DESCRIPTION, TRUE_PERFECT_BUDDYSTEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, createUpgradeIconList(), createUpgradeMaterialList());
    }

    private static List<ResourceLocation> createUpgradeIconList() {
        return List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_SWORD, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_AXE, EMPTY_SLOT_BOOTS, EMPTY_SLOT_HOE, EMPTY_SLOT_SHOVEL);
    }

    private static List<ResourceLocation> createUpgradeMaterialList() {
        return List.of(EMPTY_SLOT_INGOT);
    }
}
