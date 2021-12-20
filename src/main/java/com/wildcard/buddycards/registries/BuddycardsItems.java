package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.item.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BuddycardsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Buddycards.MOD_ID);

    public static void registerItems() {
        //Register base set
        registerCards("base", 1, 12, Rarity.COMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("base", 13, 9, Rarity.UNCOMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("base", 22, 4, Rarity.RARE, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("base", 26, 2, Rarity.EPIC, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("base", 28, 4, Rarity.COMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("base", 32, 3, Rarity.UNCOMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("base", 35, 2, Rarity.RARE, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        //Register nether set
        registerCards("nether", 1, 12, Rarity.COMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("nether", 13, 9, Rarity.UNCOMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("nether", 22, 4, Rarity.RARE, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("nether", 26, 2, Rarity.EPIC, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        //Register end set
        registerCards("end", 1, 12, Rarity.COMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("end", 13, 9, Rarity.UNCOMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("end", 22, 4, Rarity.RARE, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards("end", 26, 2, Rarity.EPIC, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Default parameters
    public static final Item.Properties DEFAULT_PROPERTIES = new Item.Properties().tab(Buddycards.TAB);
    public static final Item.Properties DEFAULT_UNCOMMON_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).rarity(Rarity.UNCOMMON);
    public static final Item.Properties DEFAULT_RARE_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).rarity(Rarity.RARE);
    public static final Item.Properties DEFAULT_EPIC_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).rarity(Rarity.EPIC);
    public static final Item.Properties DEFAULT_CARD_PROPERTIES = new Item.Properties().tab(Buddycards.CARDS_TAB);
    public static final Item.Properties DEFAULT_PACK_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).stacksTo(16).rarity(Rarity.UNCOMMON);
    public static final Item.Properties RARE_PACK_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).stacksTo(16).rarity(Rarity.RARE);
    public static final Item.Properties DEFAULT_BINDER_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).stacksTo(1).rarity(Rarity.UNCOMMON);
    public static final Item.Properties DEFAULT_MEDAL_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).stacksTo(1).rarity(Rarity.UNCOMMON);
    public static final Item.Properties BUDDYSTEEL_MEDAL_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).stacksTo(1).rarity(Rarity.COMMON);
    public static final BuddycardRequirement DEFAULT_BUDDYCARD_REQUIREMENT = () -> true;

    //Packs
    public static final RegistryObject<BuddycardPackItem> PACK_BASE = ITEMS.register("buddycard_pack_base", () -> new BuddycardPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, "base", 4, 1, DEFAULT_PACK_PROPERTIES));
    public static final RegistryObject<BuddycardPackItem> PACK_NETHER = ITEMS.register("buddycard_pack_nether", () -> new BuddycardPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, "nether", 4, 1, DEFAULT_PACK_PROPERTIES));
    public static final RegistryObject<BuddycardPackItem> PACK_END = ITEMS.register("buddycard_pack_end", () -> new BuddycardPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, "end", 4, 1, DEFAULT_PACK_PROPERTIES));
    public static final RegistryObject<BuddycardPackItem> MYSTERY_PACK = ITEMS.register("buddycard_pack_mystery", () -> new MysteryBuddycardPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, 4, 1, false, RARE_PACK_PROPERTIES));
    //Binders
    public static final RegistryObject<BuddycardBinderItem> BINDER_BASE = ITEMS.register("buddycard_binder_base", () -> new BuddycardBinderItem(DEFAULT_BUDDYCARD_REQUIREMENT, DEFAULT_BINDER_PROPERTIES));
    public static final RegistryObject<BuddycardBinderItem> BINDER_NETHER = ITEMS.register("buddycard_binder_nether", () -> new BuddycardBinderItem(DEFAULT_BUDDYCARD_REQUIREMENT, DEFAULT_BINDER_PROPERTIES));
    public static final RegistryObject<BuddycardBinderItem> BINDER_END = ITEMS.register("buddycard_binder_end", () -> new BuddycardBinderItem(DEFAULT_BUDDYCARD_REQUIREMENT, DEFAULT_BINDER_PROPERTIES));
    public static final RegistryObject<EnderBinderItem> ENDER_BINDER = ITEMS.register("ender_buddycard_binder", () -> new EnderBinderItem(DEFAULT_BINDER_PROPERTIES));
    //Buddysteel Items
    public static final RegistryObject<Item> BUDDYSTEEL_BLEND = ITEMS.register("buddysteel_blend", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> BUDDYSTEEL_INGOT = ITEMS.register("buddysteel_ingot", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> BUDDYSTEEL_NUGGET = ITEMS.register("buddysteel_nugget", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> BUDDYSTEEL_BLOCK = ITEMS.register("buddysteel_block", () -> new BlockItem(BuddycardsBlocks.BUDDYSTEEL_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BuddysteelPowerMeterItem> BUDDYSTEEL_POWER_METER = ITEMS.register("buddysteel_power_meter", () -> new BuddysteelPowerMeterItem(DEFAULT_UNCOMMON_PROPERTIES));
    //Medals
    public static final RegistryObject<BlankBuddysteelMedalItem> BLANK_BUDDYSTEEL_MEDAL = ITEMS.register("blank_buddysteel_medal", () -> new BlankBuddysteelMedalItem(BUDDYSTEEL_MEDAL_PROPERTIES));
    public static final RegistryObject<BuddysteelSetMedalItem> MEDAL_BASE = ITEMS.register("buddysteel_medal_base", () -> new BuddysteelSetMedalItem(DEFAULT_BUDDYCARD_REQUIREMENT, MedalTypes.BASE_SET, "base", DEFAULT_MEDAL_PROPERTIES));
    public static final RegistryObject<BuddysteelSetMedalItem> MEDAL_NETHER = ITEMS.register("buddysteel_medal_nether", () -> new BuddysteelSetMedalItem(DEFAULT_BUDDYCARD_REQUIREMENT, MedalTypes.NETHER_SET, "nether", DEFAULT_MEDAL_PROPERTIES));
    public static final RegistryObject<BuddysteelSetMedalItem> MEDAL_END = ITEMS.register("buddysteel_medal_end", () -> new BuddysteelSetMedalItem(DEFAULT_BUDDYCARD_REQUIREMENT, MedalTypes.END_SET, "end", DEFAULT_MEDAL_PROPERTIES));
    //Grading Sleeves
    public static final RegistryObject<GradingSleeveItem> GRADING_SLEEVE = ITEMS.register("grading_sleeve", () -> new GradingSleeveItem(DEFAULT_PROPERTIES, new float[]{0.4f,0.3f,0.225f,0.073f}));
    public static final RegistryObject<GradingSleeveItem> GOLDEN_GRADING_SLEEVE = ITEMS.register("golden_grading_sleeve", () -> new GradingSleeveItem(DEFAULT_UNCOMMON_PROPERTIES, new float[]{0.1f,0.4f,0.3f,0.195f}));
    public static final RegistryObject<GradingSleeveItem> CREATIVE_GRADING_SLEEVE = ITEMS.register("creative_grading_sleeve", () -> new CreativeGradingSleeveItem(DEFAULT_EPIC_PROPERTIES, new float[]{0.1f,0.4f,0.3f,0.19f}));
    //Card Display Items
    public static final RegistryObject<BlockItem> OAK_CARD_DISPLAY_ITEM = ITEMS.register("oak_card_display", () -> new BlockItem(BuddycardsBlocks.OAK_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> SPRUCE_CARD_DISPLAY_ITEM = ITEMS.register("spruce_card_display", () -> new BlockItem(BuddycardsBlocks.SPRUCE_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> BIRCH_CARD_DISPLAY_ITEM = ITEMS.register("birch_card_display", () -> new BlockItem(BuddycardsBlocks.BIRCH_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> JUNGLE_CARD_DISPLAY_ITEM = ITEMS.register("jungle_card_display", () -> new BlockItem(BuddycardsBlocks.JUNGLE_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> ACACIA_CARD_DISPLAY_ITEM = ITEMS.register("acacia_card_display", () -> new BlockItem(BuddycardsBlocks.ACACIA_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> DARK_OAK_CARD_DISPLAY_ITEM = ITEMS.register("dark_oak_card_display", () -> new BlockItem(BuddycardsBlocks.DARK_OAK_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> CRIMSON_CARD_DISPLAY_ITEM = ITEMS.register("crimson_card_display", () -> new BlockItem(BuddycardsBlocks.CRIMSON_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> WARPED_CARD_DISPLAY_ITEM = ITEMS.register("warped_card_display", () -> new BlockItem(BuddycardsBlocks.WARPED_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    //Booster Box Items
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_BASE = ITEMS.register("buddycard_booster_box_base", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_BASE.get(), "base", DEFAULT_UNCOMMON_PROPERTIES));
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_NETHER = ITEMS.register("buddycard_booster_box_nether", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_NETHER.get(), "nether", DEFAULT_UNCOMMON_PROPERTIES));
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_END = ITEMS.register("buddycard_booster_box_end", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_END.get(), "end", DEFAULT_UNCOMMON_PROPERTIES));
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_MYSTERY = ITEMS.register("buddycard_booster_box_mystery", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_MYSTERY.get(), "mystery", DEFAULT_EPIC_PROPERTIES));

    //Card registration
    public static void registerCards(String set, int startValue, int amount, Rarity rarity, Item.Properties properties, BuddycardRequirement requirement) {
        for(int i = startValue; i < amount + startValue; i++) {
            ITEMS.register("buddycard_" + set + i, new BuddycardItem(requirement, set, i, rarity, properties).delegate);
        }
    }

    public interface BuddycardRequirement {
        boolean shouldLoad();
    }
}
