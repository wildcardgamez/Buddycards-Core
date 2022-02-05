package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.item.*;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class BuddycardsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Buddycards.MOD_ID);

    public static void registerItems() {
        //Register base set
        registerCards(BASE_SET, 1, 12, Rarity.COMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(BASE_SET, 13, 9, Rarity.UNCOMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(BASE_SET, 22, 4, Rarity.RARE, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(BASE_SET, 26, 2, Rarity.EPIC, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(BASE_SET, 28, 4, Rarity.COMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(BASE_SET, 32, 3, Rarity.UNCOMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(BASE_SET, 35, 2, Rarity.RARE, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        //Register nether set
        registerCards(NETHER_SET, 1, 12, Rarity.COMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(NETHER_SET, 13, 9, Rarity.UNCOMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(NETHER_SET, 22, 4, Rarity.RARE, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(NETHER_SET, 26, 2, Rarity.EPIC, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        //Register end set
        registerCards(END_SET, 1, 12, Rarity.COMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(END_SET, 13, 9, Rarity.UNCOMMON, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(END_SET, 22, 4, Rarity.RARE, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);
        registerCards(END_SET, 26, 2, Rarity.EPIC, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT);

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final BuddycardSet BASE_SET = new BuddycardSet("base");
    public static final BuddycardSet NETHER_SET = new BuddycardSet("nether");
    public static final BuddycardSet END_SET = new BuddycardSet("end");

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
    public static final SimpleWeightedRandomList<Rarity> DEFAULT_RARITY_WEIGHTS = SimpleWeightedRandomList.<Rarity>builder()
            .add(Rarity.COMMON, 24)
            .add(Rarity.UNCOMMON, 12)
            .add(Rarity.RARE, 3)
            .add(Rarity.EPIC, 1)
            .build();

    //Packs
    public static final RegistryObject<BuddycardPackItem> PACK_BASE = ITEMS.register("buddycard_pack_base", () -> new BuddycardSetPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, BASE_SET, 4, 1, DEFAULT_RARITY_WEIGHTS, DEFAULT_PACK_PROPERTIES));
    public static final RegistryObject<BuddycardPackItem> PACK_NETHER = ITEMS.register("buddycard_pack_nether", () -> new BuddycardSetPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, NETHER_SET, 4, 1, DEFAULT_RARITY_WEIGHTS, DEFAULT_PACK_PROPERTIES));
    public static final RegistryObject<BuddycardPackItem> PACK_END = ITEMS.register("buddycard_pack_end", () -> new BuddycardSetPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, END_SET, 4, 1, DEFAULT_RARITY_WEIGHTS, DEFAULT_PACK_PROPERTIES));
    public static final RegistryObject<BuddycardPackItem> MYSTERY_PACK = ITEMS.register("buddycard_pack_mystery", () -> new MysteryBuddycardPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, 4, 1, DEFAULT_RARITY_WEIGHTS, false, RARE_PACK_PROPERTIES));
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
    public static final RegistryObject<BuddysteelSetMedalItem> MEDAL_BASE = ITEMS.register("buddysteel_medal_base", () -> new BuddysteelSetMedalItem(DEFAULT_BUDDYCARD_REQUIREMENT, MedalTypes.BASE_SET, BASE_SET, DEFAULT_MEDAL_PROPERTIES));
    public static final RegistryObject<BuddysteelSetMedalItem> MEDAL_NETHER = ITEMS.register("buddysteel_medal_nether", () -> new BuddysteelSetMedalItem(DEFAULT_BUDDYCARD_REQUIREMENT, MedalTypes.NETHER_SET, NETHER_SET, DEFAULT_MEDAL_PROPERTIES));
    public static final RegistryObject<BuddysteelSetMedalItem> MEDAL_END = ITEMS.register("buddysteel_medal_end", () -> new BuddysteelSetMedalItem(DEFAULT_BUDDYCARD_REQUIREMENT, MedalTypes.END_SET, END_SET, DEFAULT_MEDAL_PROPERTIES));
    //Grading Sleeves
    public static final RegistryObject<GradingSleeveItem> GRADING_SLEEVE = ITEMS.register("grading_sleeve", () -> new GradingSleeveItem(DEFAULT_PROPERTIES, new float[]{0.4f, 0.3f, 0.225f, 0.073f}));
    public static final RegistryObject<GradingSleeveItem> GOLDEN_GRADING_SLEEVE = ITEMS.register("golden_grading_sleeve", () -> new GradingSleeveItem(DEFAULT_UNCOMMON_PROPERTIES, new float[]{0.1f, 0.4f, 0.3f, 0.195f}));
    public static final RegistryObject<GradingSleeveItem> CREATIVE_GRADING_SLEEVE = ITEMS.register("creative_grading_sleeve", () -> new CreativeGradingSleeveItem(DEFAULT_EPIC_PROPERTIES, new float[]{0.1f, 0.4f, 0.3f, 0.19f}));
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
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_BASE = ITEMS.register("buddycard_booster_box_base", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_BASE.get(), PACK_BASE, DEFAULT_UNCOMMON_PROPERTIES));
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_NETHER = ITEMS.register("buddycard_booster_box_nether", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_NETHER.get(), PACK_NETHER, DEFAULT_UNCOMMON_PROPERTIES));
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_END = ITEMS.register("buddycard_booster_box_end", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_END.get(), PACK_END, DEFAULT_UNCOMMON_PROPERTIES));
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_MYSTERY = ITEMS.register("buddycard_booster_box_mystery", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_MYSTERY.get(), MYSTERY_PACK, DEFAULT_EPIC_PROPERTIES));

    //Card registration
    public static void registerCards(BuddycardSet set, int startValue, int amount, Rarity rarity, Item.Properties properties, BuddycardRequirement requirement) {
        Objects.requireNonNull(set);
        for (int i = startValue; i < amount + startValue; i++) {
            ITEMS.register("buddycard_" + set.getName() + i, new BuddycardItem(requirement, set, i, rarity, properties).delegate);
        }
    }

    public interface BuddycardRequirement {
        boolean shouldLoad();
    }
}
