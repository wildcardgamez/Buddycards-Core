package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.gear.BuddycardsToolTier;
import com.wildcard.buddycards.gear.MedalTypes;
import com.wildcard.buddycards.integration.CuriosIntegration;
import com.wildcard.buddycards.item.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.Calendar;

public class BuddycardsItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Buddycards.MOD_ID);

    public static void registerItems(IEventBus eventBus) {
        register36Set(BASE_SET);
        register27Set(NETHER_SET);
        register27Set(END_SET);
        register27Set(CAVE_SET);
        registerCards(HALLOWEEN_BUDDYCARD_REQUIREMENT, HOLIDAY_SET, 1, 3, Rarity.UNCOMMON);
        registerCards(CHRISTMAS_BUDDYCARD_REQUIREMENT, HOLIDAY_SET, 4, 3, Rarity.UNCOMMON);
        registerCards(ANNIVERSARY_BUDDYCARD_REQUIREMENT, BPC_SET, 1, 5, Rarity.EPIC);
        ITEMS.register(eventBus);
    }

    public static final BuddycardSet BASE_SET = new BuddycardSet("base");
    public static final BuddycardSet NETHER_SET = new BuddycardSet("nether");
    public static final BuddycardSet END_SET = new BuddycardSet("end");
    public static final BuddycardSet CAVE_SET = new BuddycardSet("cave");
    public static final BuddycardSet HOLIDAY_SET = new BuddycardSet("holiday", true);
    public static final BuddycardSet BPC_SET = new BuddycardSet("bpc", true);

    //Properties
    public static final Item.Properties DEFAULT_PROPERTIES = new Item.Properties();
    public static final Item.Properties DEFAULT_UNCOMMON_PROPERTIES = new Item.Properties().rarity(Rarity.UNCOMMON);
    public static final Item.Properties UNCOMMON_TOOL_PROPERTIES = new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1);
    public static final Item.Properties RARE_TOOL_PROPERTIES = new Item.Properties().rarity(Rarity.RARE).stacksTo(1);
    public static final Item.Properties DEFAULT_RARE_PROPERTIES = new Item.Properties().rarity(Rarity.RARE);
    public static final Item.Properties DEFAULT_EPIC_PROPERTIES = new Item.Properties().rarity(Rarity.EPIC);
    public static final Item.Properties DEFAULT_PACK_PROPERTIES = new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON);
    public static final Item.Properties RARE_PACK_PROPERTIES = new Item.Properties().stacksTo(16).rarity(Rarity.RARE);
    public static final Item.Properties DEFAULT_BINDER_PROPERTIES = new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
    public static final Item.Properties DEFAULT_CURIO_PROPERTIES = new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON);

    public static final BuddycardRequirement DEFAULT_BUDDYCARD_REQUIREMENT = () -> true;
    public static final BuddycardRequirement HALLOWEEN_BUDDYCARD_REQUIREMENT = () -> Calendar.getInstance().get(Calendar.MONTH) == Calendar.OCTOBER && Calendar.getInstance().get(Calendar.DATE) >= 29;
    public static final BuddycardRequirement CHRISTMAS_BUDDYCARD_REQUIREMENT = () -> Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER && Calendar.getInstance().get(Calendar.DATE) >= 24 && Calendar.getInstance().get(Calendar.DATE) <= 26;
    public static final BuddycardRequirement ANNIVERSARY_BUDDYCARD_REQUIREMENT = () -> Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER && Calendar.getInstance().get(Calendar.DATE) >= 5 && Calendar.getInstance().get(Calendar.DATE) <= 8;

    public static final SimpleWeightedRandomList<Rarity> DEFAULT_RARITY_WEIGHTS = SimpleWeightedRandomList.<Rarity>builder()
            .add(Rarity.COMMON, 24)
            .add(Rarity.UNCOMMON, 12)
            .add(Rarity.RARE, 3)
            .add(Rarity.EPIC, 1)
            .build();

    //Packs
    public static final DeferredItem<BuddycardPackItem> PACK_BASE = ITEMS.register("buddycard_pack_base", () -> new BuddycardSetPackItem(BASE_SET, 4, 1, DEFAULT_RARITY_WEIGHTS, DEFAULT_PACK_PROPERTIES));
    public static final DeferredItem<BuddycardPackItem> PACK_NETHER = ITEMS.register("buddycard_pack_nether", () -> new BuddycardSetPackItem(NETHER_SET, 4, 1, DEFAULT_RARITY_WEIGHTS, DEFAULT_PACK_PROPERTIES));
    public static final DeferredItem<BuddycardPackItem> PACK_END = ITEMS.register("buddycard_pack_end", () -> new BuddycardSetPackItem(END_SET, 4, 1, DEFAULT_RARITY_WEIGHTS, DEFAULT_PACK_PROPERTIES));
    public static final DeferredItem<BuddycardPackItem> PACK_CAVE = ITEMS.register("buddycard_pack_cave", () -> new BuddycardSetPackItem(CAVE_SET, 4, 1, DEFAULT_RARITY_WEIGHTS, DEFAULT_PACK_PROPERTIES));
    public static final DeferredItem<BuddycardPackItem> MYSTERY_PACK = ITEMS.register("buddycard_pack_mystery", () -> new MysteryBuddycardPackItem(4, 1, DEFAULT_RARITY_WEIGHTS, false, RARE_PACK_PROPERTIES));
    //Binders
    public static final DeferredItem<BuddycardBinderItem> BINDER_BASE = ITEMS.register("buddycard_binder_base", () -> new BuddycardBinderItem(DEFAULT_BINDER_PROPERTIES, BASE_SET, Buddycards.buddycardsLocation("textures/gui/buddycard_binder_base.png"), false));
    public static final DeferredItem<BuddycardBinderItem> BINDER_NETHER = ITEMS.register("buddycard_binder_nether", () -> new BuddycardBinderItem(DEFAULT_BINDER_PROPERTIES, NETHER_SET, Buddycards.buddycardsLocation("textures/gui/buddycard_binder_nether.png"), false));
    public static final DeferredItem<BuddycardBinderItem> BINDER_END = ITEMS.register("buddycard_binder_end", () -> new BuddycardBinderItem(DEFAULT_BINDER_PROPERTIES, END_SET, Buddycards.buddycardsLocation("textures/gui/buddycard_binder_end.png"), false));
    public static final DeferredItem<BuddycardBinderItem> BINDER_CAVE = ITEMS.register("buddycard_binder_cave", () -> new BuddycardBinderItem(DEFAULT_BINDER_PROPERTIES, CAVE_SET, Buddycards.buddycardsLocation("textures/gui/buddycard_binder_cave.png"), false));
    public static final DeferredItem<BuddycardBinderItem> LARGE_BINDER_BASE = ITEMS.register("large_buddycard_binder_base", () -> new BuddycardBinderItem(DEFAULT_BINDER_PROPERTIES, BASE_SET, Buddycards.buddycardsLocation("textures/gui/large_buddycard_binder_base.png"), true));
    public static final DeferredItem<BuddycardBinderItem> LARGE_BINDER_NETHER = ITEMS.register("large_buddycard_binder_nether", () -> new BuddycardBinderItem(DEFAULT_BINDER_PROPERTIES, NETHER_SET, Buddycards.buddycardsLocation("textures/gui/large_buddycard_binder_nether.png"), true));
    public static final DeferredItem<BuddycardBinderItem> LARGE_BINDER_END = ITEMS.register("large_buddycard_binder_end", () -> new BuddycardBinderItem(DEFAULT_BINDER_PROPERTIES, END_SET, Buddycards.buddycardsLocation("textures/gui/large_buddycard_binder_end.png"), true));
    public static final DeferredItem<BuddycardBinderItem> LARGE_BINDER_CAVE = ITEMS.register("large_buddycard_binder_cave", () -> new BuddycardBinderItem(DEFAULT_BINDER_PROPERTIES, CAVE_SET, Buddycards.buddycardsLocation("textures/gui/large_buddycard_binder_cave.png"), true));
    //Buddysteel Items
    public static final DeferredItem<Item> BUDDYSTEEL_BLEND = ITEMS.register("buddysteel_blend", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> BUDDYSTEEL_INGOT = ITEMS.register("buddysteel_ingot", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> BUDDYSTEEL_NUGGET = ITEMS.register("buddysteel_nugget", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> BUDDYSTEEL_BLOCK = ITEMS.register("buddysteel_block", () -> new BlockItem(BuddycardsBlocks.BUDDYSTEEL_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> BUDDYSTEEL_SCANNER = ITEMS.register("buddysteel_scanner", () -> new BuddysteelScannerItem());
    public static final DeferredItem<Item> BUDDYSTEEL_KEY = ITEMS.register("buddysteel_key", BuddysteelKeyItem::new);
    //public static final DeferredItem<Item> BUDDYSTEEL_HELMET = ITEMS.register("buddysteel_helmet", () -> new BuddycardsArmorItem(BuddycardsMisc.BUDDYSTEEL_ARMOR, ArmorItem.Type.HELMET));
    //public static final DeferredItem<Item> BUDDYSTEEL_CHESTPLATE = ITEMS.register("buddysteel_chestplate", () -> new BuddycardsArmorItem(BuddycardsMisc.BUDDYSTEEL_ARMOR, ArmorItem.Type.CHESTPLATE));
    //public static final DeferredItem<Item> BUDDYSTEEL_LEGGINGS = ITEMS.register("buddysteel_leggings", () -> new BuddycardsArmorItem(BuddycardsMisc.BUDDYSTEEL_ARMOR, ArmorItem.Type.LEGGINGS));
    //public static final DeferredItem<Item> BUDDYSTEEL_BOOTS = ITEMS.register("buddysteel_boots", () -> new BuddycardsArmorItem(BuddycardsMisc.BUDDYSTEEL_ARMOR, ArmorItem.Type.BOOTS));
    //public static final DeferredItem<Item> BUDDYSTEEL_SWORD = ITEMS.register("buddysteel_sword", () -> new SwordItem(BuddycardsToolTier.BUDDYSTEEL, UNCOMMON_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> BUDDYSTEEL_SHOVEL = ITEMS.register("buddysteel_shovel", () -> new ShovelItem(BuddycardsToolTier.BUDDYSTEEL, UNCOMMON_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> BUDDYSTEEL_PICKAXE = ITEMS.register("buddysteel_pickaxe", () -> new PickaxeItem(BuddycardsToolTier.BUDDYSTEEL, UNCOMMON_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> BUDDYSTEEL_AXE = ITEMS.register("buddysteel_axe", () -> new AxeItem(BuddycardsToolTier.BUDDYSTEEL, UNCOMMON_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> BUDDYSTEEL_HOE = ITEMS.register("buddysteel_hoe", () -> new HoeItem(BuddycardsToolTier.BUDDYSTEEL, UNCOMMON_TOOL_PROPERTIES));
    public static final DeferredItem<Item> BUDDYSTEEL_RING = ITEMS.register("buddysteel_ring", () -> new AttributeCurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
            (m) -> m.put(BuddycardsAttributes.BUDDY_BONUS, new AttributeModifier(Buddycards.buddycardsLocation("buddysteel_ring"), 0.25, AttributeModifier.Operation.ADD_VALUE))));
    //Charger
    public static final DeferredItem<BlockItem> CHARGER = ITEMS.register("buddysteel_charger", () -> new DescriptionBlockItem(BuddycardsBlocks.CHARGER.get(), DEFAULT_PROPERTIES));
    //Medals
    public static final DeferredItem<Item> BLANK_BUDDYSTEEL_MEDAL = ITEMS.register("blank_buddysteel_medal", () -> new Item(DEFAULT_CURIO_PROPERTIES));
    public static final DeferredItem<BuddysteelSetMedalItem> MEDAL_BASE = ITEMS.register("buddysteel_medal_base", () -> new BuddysteelSetMedalItem(MedalTypes.BASE_SET, BASE_SET, new Item.Properties().stacksTo(1).component(BuddycardsComponents.COLLECTION_TIER, 0)));
    public static final DeferredItem<BuddysteelSetMedalItem> MEDAL_NETHER = ITEMS.register("buddysteel_medal_nether", () -> new BuddysteelSetMedalItem(MedalTypes.NETHER_SET, NETHER_SET, new Item.Properties().stacksTo(1).component(BuddycardsComponents.COLLECTION_TIER, 0)));
    public static final DeferredItem<BuddysteelSetMedalItem> MEDAL_END = ITEMS.register("buddysteel_medal_end", () -> new BuddysteelSetMedalItem(MedalTypes.END_SET, END_SET, new Item.Properties().stacksTo(1).component(BuddycardsComponents.COLLECTION_TIER, 0)));
    public static final DeferredItem<BuddysteelSetMedalItem> MEDAL_CAVE = ITEMS.register("buddysteel_medal_cave", () -> new BuddysteelSetMedalItem(MedalTypes.CAVE_SET, CAVE_SET, new Item.Properties().stacksTo(1).component(BuddycardsComponents.COLLECTION_TIER, 0)));
    //Sleeves
    public static final DeferredItem<GradingSleeveItem> GRADING_SLEEVE = ITEMS.register("grading_sleeve", () -> new GradingSleeveItem(DEFAULT_PROPERTIES, new float[]{0.4f, 0.3f, 0.225f, 0.073f}));
    public static final DeferredItem<GradingSleeveItem> GOLDEN_GRADING_SLEEVE = ITEMS.register("golden_grading_sleeve", () -> new GradingSleeveItem(DEFAULT_UNCOMMON_PROPERTIES, new float[]{0.1f, 0.4f, 0.3f, 0.195f}));
    public static final DeferredItem<LuminisSleeveItem> LUMINIS_SLEEVE = ITEMS.register("luminis_sleeve", () -> new LuminisSleeveItem(DEFAULT_RARE_PROPERTIES));
    public static final DeferredItem<GradingSleeveItem> ZYLEX_GRADING_SLEEVE = ITEMS.register("zylex_grading_sleeve", () -> new GradingSleeveItem(DEFAULT_RARE_PROPERTIES, new float[]{0, 0, 0.25f, 0.6f}));
    //Card Display Items
    public static final DeferredItem<BlockItem> OAK_CARD_DISPLAY_ITEM = ITEMS.register("oak_card_display", () -> new BlockItem(BuddycardsBlocks.OAK_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> SPRUCE_CARD_DISPLAY_ITEM = ITEMS.register("spruce_card_display", () -> new BlockItem(BuddycardsBlocks.SPRUCE_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> BIRCH_CARD_DISPLAY_ITEM = ITEMS.register("birch_card_display", () -> new BlockItem(BuddycardsBlocks.BIRCH_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> JUNGLE_CARD_DISPLAY_ITEM = ITEMS.register("jungle_card_display", () -> new BlockItem(BuddycardsBlocks.JUNGLE_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> ACACIA_CARD_DISPLAY_ITEM = ITEMS.register("acacia_card_display", () -> new BlockItem(BuddycardsBlocks.ACACIA_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> DARK_OAK_CARD_DISPLAY_ITEM = ITEMS.register("dark_oak_card_display", () -> new BlockItem(BuddycardsBlocks.DARK_OAK_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> CRIMSON_CARD_DISPLAY_ITEM = ITEMS.register("crimson_card_display", () -> new BlockItem(BuddycardsBlocks.CRIMSON_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> WARPED_CARD_DISPLAY_ITEM = ITEMS.register("warped_card_display", () -> new BlockItem(BuddycardsBlocks.WARPED_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> MANGROVE_CARD_DISPLAY_ITEM = ITEMS.register("mangrove_card_display", () -> new BlockItem(BuddycardsBlocks.MANGROVE_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> CHERRY_CARD_DISPLAY_ITEM = ITEMS.register("cherry_card_display", () -> new BlockItem(BuddycardsBlocks.CHERRY_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> BAMBOO_CARD_DISPLAY_ITEM = ITEMS.register("bamboo_card_display", () -> new BlockItem(BuddycardsBlocks.BAMBOO_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    //Card Stand Items
    public static final DeferredItem<BlockItem> STONE_CARD_STAND_ITEM = ITEMS.register("stone_card_stand", () -> new BlockItem(BuddycardsBlocks.STONE_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> DEEPSLATE_CARD_STAND = ITEMS.register("deepslate_card_stand", () -> new BlockItem(BuddycardsBlocks.DEEPSLATE_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> BLACKSTONE_CARD_STAND_ITEM = ITEMS.register("blackstone_card_stand", () -> new BlockItem(BuddycardsBlocks.BLACKSTONE_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> ANDESITE_CARD_STAND_ITEM = ITEMS.register("andesite_card_stand", () -> new BlockItem(BuddycardsBlocks.ANDESITE_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> DIORITE_CARD_STAND_ITEM = ITEMS.register("diorite_card_stand", () -> new BlockItem(BuddycardsBlocks.DIORITE_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> GRANITE_CARD_STAND_ITEM = ITEMS.register("granite_card_stand", () -> new BlockItem(BuddycardsBlocks.GRANITE_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> SANDSTONE_CARD_STAND_ITEM = ITEMS.register("sandstone_card_stand", () -> new BlockItem(BuddycardsBlocks.SANDSTONE_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> RED_SANDSTONE_CARD_STAND_ITEM = ITEMS.register("red_sandstone_card_stand", () -> new BlockItem(BuddycardsBlocks.RED_SANDSTONE_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> PRISMARINE_CARD_STAND_ITEM = ITEMS.register("prismarine_card_stand", () -> new BlockItem(BuddycardsBlocks.PRISMARINE_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> CALCITE_CARD_STAND_ITEM = ITEMS.register("calcite_card_stand", () -> new BlockItem(BuddycardsBlocks.CALCITE_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> TUFF_CARD_STAND_ITEM = ITEMS.register("tuff_card_stand", () -> new BlockItem(BuddycardsBlocks.TUFF_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> DRIPSTONE_CARD_STAND_ITEM = ITEMS.register("dripstone_card_stand", () -> new BlockItem(BuddycardsBlocks.DRIPSTONE_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> BASALT_CARD_STAND_ITEM = ITEMS.register("basalt_card_stand", () -> new BlockItem(BuddycardsBlocks.BASALT_CARD_STAND.get(), DEFAULT_PROPERTIES));
    //Booster Box Items
    public static final DeferredItem<BuddycardBoosterBoxItem> BOOSTER_BOX_BASE = ITEMS.register("buddycard_booster_box_base", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_BASE.get(), PACK_BASE, DEFAULT_UNCOMMON_PROPERTIES));
    public static final DeferredItem<BuddycardBoosterBoxItem> BOOSTER_BOX_NETHER = ITEMS.register("buddycard_booster_box_nether", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_NETHER.get(), PACK_NETHER, DEFAULT_UNCOMMON_PROPERTIES));
    public static final DeferredItem<BuddycardBoosterBoxItem> BOOSTER_BOX_END = ITEMS.register("buddycard_booster_box_end", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_END.get(), PACK_END, DEFAULT_UNCOMMON_PROPERTIES));
    public static final DeferredItem<BuddycardBoosterBoxItem> BOOSTER_BOX_CAVE = ITEMS.register("buddycard_booster_box_cave", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_CAVE.get(), PACK_CAVE, DEFAULT_UNCOMMON_PROPERTIES));
    public static final DeferredItem<BuddycardBoosterBoxItem> BOOSTER_BOX_MYSTERY = ITEMS.register("buddycard_booster_box_mystery", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_MYSTERY.get(), MYSTERY_PACK, DEFAULT_EPIC_PROPERTIES));
    //Luminis Items
    public static final DeferredItem<BlockItem> LUMINIS_ORE = ITEMS.register("luminis_ore", () -> new BlockItem(BuddycardsBlocks.LUMINIS_ORE.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> DEEPSLATE_LUMINIS_ORE = ITEMS.register("deepslate_luminis_ore", () -> new BlockItem(BuddycardsBlocks.DEEPSLATE_LUMINIS_ORE.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> LUMINIS_CRYSTAL = ITEMS.register("luminis_crystal", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> LUMINIS_CRYSTAL_BLOCK = ITEMS.register("luminis_crystal_block", () -> new BlockItem(BuddycardsBlocks.LUMINIS_CRYSTAL_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> LUMINIS = ITEMS.register("luminis", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> LUMINIS_BLOCK = ITEMS.register("luminis_block", () -> new BlockItem(BuddycardsBlocks.LUMINIS_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> LUMINIS_PANEL = ITEMS.register("luminis_panel", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> LUMINIS_PANELS = ITEMS.register("luminis_panels", () -> new BlockItem(BuddycardsBlocks.LUMINIS_PANELS.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> LUMINIS_SLAB = ITEMS.register("luminis_slab", () -> new BlockItem(BuddycardsBlocks.LUMINIS_SLAB.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> LUMINIS_STAIRS = ITEMS.register("luminis_stairs", () -> new BlockItem(BuddycardsBlocks.LUMINIS_STAIRS.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> LUMINIS_PILLAR = ITEMS.register("luminis_pillar", () -> new BlockItem(BuddycardsBlocks.LUMINIS_PILLAR.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> LUMINIS_CRYSTAL_PILLAR = ITEMS.register("luminis_crystal_pillar", () -> new BlockItem(BuddycardsBlocks.LUMINIS_CRYSTAL_PILLAR.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> LUMINIS_CARD_DISPLAY = ITEMS.register("luminis_card_display", () -> new BlockItem(BuddycardsBlocks.LUMINIS_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> LUMINIS_CARD_STAND = ITEMS.register("luminis_card_stand", () -> new BlockItem(BuddycardsBlocks.LUMINIS_CARD_STAND.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> CRIMSON_LUMINIS = ITEMS.register("crimson_luminis", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> CRIMSON_LUMINIS_BLOCK = ITEMS.register("crimson_luminis_block", () -> new BlockItem(BuddycardsBlocks.CRIMSON_LUMINIS_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> KINETIC_CHAMBER = ITEMS.register("kinetic_chamber", () -> new DescriptionBlockItem(BuddycardsBlocks.KINETIC_CHAMBER.get(), DEFAULT_PROPERTIES));
    //public static final DeferredItem<Item> LUMINIS_HELMET = ITEMS.register("luminis_helmet", () -> new BuddycardsArmorItem(BuddycardsMisc.LUMINIS_ARMOR, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)
    //        .attributes(ItemAttributeModifiers.builder().add(BuddycardsAttributes.FOIL_LUCK, (new AttributeModifier(Buddycards.buddycardsLocation("foil_luck"), 0.5, AttributeModifier.Operation.ADD_VALUE)), EquipmentSlotGroup.HEAD).build())));
    public static final DeferredItem<Item> LUMINIS_PICKAXE = ITEMS.register("luminis_pickaxe", () -> new PickaxeItem(BuddycardsToolTier.LUMINIS, UNCOMMON_TOOL_PROPERTIES));
    public static final DeferredItem<Item> LUMINIS_RING = ITEMS.register("luminis_ring", () -> new AttributeCurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
            (m) -> m.put(BuddycardsAttributes.FOIL_BONUS, new AttributeModifier(Buddycards.buddycardsLocation("luminis_ring"), 0.5, AttributeModifier.Operation.ADD_VALUE))));

    public static final DeferredItem<Item> LUMINIS_SCANNER_CHIP = ITEMS.register("luminis_scanner_chip", () -> new DescriptionItem(DEFAULT_PROPERTIES));
    //Zylex Items
    public static final DeferredItem<Item> ZYLEX = ITEMS.register("zylex", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> ZYLEX_NUGGET = ITEMS.register("zylex_nugget", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> ZYLEX_BLOCK = ITEMS.register("zylex_block", () -> new BlockItem(BuddycardsBlocks.ZYLEX_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> GRADER = ITEMS.register("grader", () -> new DescriptionBlockItem(BuddycardsBlocks.GRADER.get(), DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> VOID_ZYLEX = ITEMS.register("void_zylex", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<BlockItem> VOID_ZYLEX_BLOCK = ITEMS.register("void_zylex_block", () -> new BlockItem(BuddycardsBlocks.VOID_ZYLEX_BLOCK.get(), DEFAULT_PROPERTIES));
    //public static final DeferredItem<Item> ZYLEX_BOOTS = ITEMS.register("zylex_boots", () -> new BuddycardsArmorItem(BuddycardsMisc.ZYLEX_ARMOR, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)
    //        .attributes(ItemAttributeModifiers.builder().add(BuddycardsAttributes.GRADING_LUCK, (new AttributeModifier(Buddycards.buddycardsLocation("grading_luck"), 0.5, AttributeModifier.Operation.ADD_VALUE)), EquipmentSlotGroup.FEET).build())));
    public static final DeferredItem<Item> ZYLEX_HOE = ITEMS.register("zylex_hoe", () -> new HoeItem(BuddycardsToolTier.ZYLEX, UNCOMMON_TOOL_PROPERTIES));
    public static final DeferredItem<Item> ZYLEX_RING = ITEMS.register("zylex_ring", () -> new AttributeCurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
            (m) -> m.put(BuddycardsAttributes.GRADING_LUCK, new AttributeModifier(Buddycards.buddycardsLocation("zylex_ring"), 0.5, AttributeModifier.Operation.ADD_VALUE))));
    public static final DeferredItem<Item> ZYLEX_SCANNER_CHIP = ITEMS.register("zylex_scanner_chip", () -> new DescriptionItem(DEFAULT_PROPERTIES));
    //Charged Buddysteel Items
    public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_INGOT = ITEMS.register("charged_buddysteel_ingot", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_BLOCK = ITEMS.register("charged_buddysteel_block", () -> new BlockItem(BuddycardsBlocks.CHARGED_BUDDYSTEEL_BLOCK.get(), DEFAULT_PROPERTIES));
    //public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_HELMET = ITEMS.register("charged_buddysteel_helmet", () -> new BuddycardsArmorItem(BuddycardsMisc.CHARGED_BUDDYSTEEL_ARMOR, ArmorItem.Type.HELMET));
    //public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_CHESTPLATE = ITEMS.register("charged_buddysteel_chestplate", () -> new BuddycardsArmorItem(BuddycardsMisc.CHARGED_BUDDYSTEEL_ARMOR, ArmorItem.Type.CHESTPLATE));
    //public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_LEGGINGS = ITEMS.register("charged_buddysteel_leggings", () -> new BuddycardsArmorItem(BuddycardsMisc.CHARGED_BUDDYSTEEL_ARMOR, ArmorItem.Type.LEGGINGS));
    //public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_BOOTS = ITEMS.register("charged_buddysteel_boots", () -> new BuddycardsArmorItem(BuddycardsMisc.CHARGED_BUDDYSTEEL_ARMOR, ArmorItem.Type.BOOTS));
    //public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_SWORD = ITEMS.register("charged_buddysteel_sword", () -> new SwordItem(BuddycardsToolTier.CHARGED_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_SHOVEL = ITEMS.register("charged_buddysteel_shovel", () -> new ShovelItem(BuddycardsToolTier.CHARGED_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_PICKAXE = ITEMS.register("charged_buddysteel_pickaxe", () -> new PickaxeItem(BuddycardsToolTier.CHARGED_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_AXE = ITEMS.register("charged_buddysteel_axe", () -> new AxeItem(BuddycardsToolTier.CHARGED_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_HOE = ITEMS.register("charged_buddysteel_hoe", () -> new HoeItem(BuddycardsToolTier.CHARGED_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    public static final DeferredItem<Item> CHARGED_BUDDYSTEEL_RING = ITEMS.register("charged_buddysteel_ring", () -> new AttributeCurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), (m) -> {
        m.put(BuddycardsAttributes.BUDDY_BONUS, new AttributeModifier(Buddycards.buddycardsLocation("charged_buddysteel_ring"), 1, AttributeModifier.Operation.ADD_VALUE));
        m.put(BuddycardsAttributes.FOIL_BONUS, new AttributeModifier(Buddycards.buddycardsLocation("charged_buddysteel_ring"), 0.5, AttributeModifier.Operation.ADD_VALUE));
        m.put(BuddycardsAttributes.GRADING_BONUS, new AttributeModifier(Buddycards.buddycardsLocation("charged_buddysteel_ring"), 0.5, AttributeModifier.Operation.ADD_VALUE));
    }));
    public static final DeferredItem<Item> PERFECT_BUDDYSTEEL_INGOT = ITEMS.register("perfect_buddysteel_ingot", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> PERFECT_BUDDYSTEEL_BLOCK = ITEMS.register("perfect_buddysteel_block", () -> new BlockItem(BuddycardsBlocks.PERFECT_BUDDYSTEEL_BLOCK.get(), DEFAULT_PROPERTIES));
    //public static final DeferredItem<Item> PERFECT_BUDDYSTEEL_HELMET = ITEMS.register("perfect_buddysteel_helmet", () -> new BuddycardsArmorItem(BuddycardsMisc.PERFECT_BUDDYSTEEL_ARMOR, ArmorItem.Type.HELMET));
    //public static final DeferredItem<Item> PERFECT_BUDDYSTEEL_CHESTPLATE = ITEMS.register("perfect_buddysteel_chestplate", () -> new BuddycardsArmorItem(BuddycardsMisc.PERFECT_BUDDYSTEEL_ARMOR, ArmorItem.Type.CHESTPLATE));
    //public static final DeferredItem<Item> PERFECT_BUDDYSTEEL_LEGGINGS = ITEMS.register("perfect_buddysteel_leggings", () -> new BuddycardsArmorItem(BuddycardsMisc.PERFECT_BUDDYSTEEL_ARMOR, ArmorItem.Type.LEGGINGS));
    //public static final DeferredItem<Item> PERFECT_BUDDYSTEEL_BOOTS = ITEMS.register("perfect_buddysteel_boots", () -> new BuddycardsArmorItem(BuddycardsMisc.PERFECT_BUDDYSTEEL_ARMOR, ArmorItem.Type.BOOTS));
    //public static final DeferredItem<Item> PERFECT_BUDDYSTEEL_SWORD = ITEMS.register("perfect_buddysteel_sword", () -> new SwordItem(BuddycardsToolTier.PERFECT_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> PERFECT_BUDDYSTEEL_SHOVEL = ITEMS.register("perfect_buddysteel_shovel", () -> new ShovelItem(BuddycardsToolTier.PERFECT_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> PERFECT_BUDDYSTEEL_PICKAXE = ITEMS.register("perfect_buddysteel_pickaxe", () -> new PickaxeItem(BuddycardsToolTier.PERFECT_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> PERFECT_BUDDYSTEEL_AXE = ITEMS.register("perfect_buddysteel_axe", () -> new AxeItem(BuddycardsToolTier.PERFECT_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> PERFECT_BUDDYSTEEL_HOE = ITEMS.register("perfect_buddysteel_hoe", () -> new HoeItem(BuddycardsToolTier.PERFECT_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    public static final DeferredItem<Item> TRUE_PERFECT_BUDDYSTEEL_INGOT = ITEMS.register("true_perfect_buddysteel_ingot", () -> new Item(DEFAULT_PROPERTIES));
    public static final DeferredItem<Item> TRUE_PERFECT_BUDDYSTEEL_BLOCK = ITEMS.register("true_perfect_buddysteel_block", () -> new BlockItem(BuddycardsBlocks.TRUE_PERFECT_BUDDYSTEEL_BLOCK.get(), DEFAULT_PROPERTIES));
    //public static final DeferredItem<Item> TRUE_PERFECT_BUDDYSTEEL_HELMET = ITEMS.register("true_perfect_buddysteel_helmet", () -> new BuddycardsArmorItem(BuddycardsMisc.TRUE_PERFECT_BUDDYSTEEL_ARMOR, ArmorItem.Type.HELMET));
    //public static final DeferredItem<Item> TRUE_PERFECT_BUDDYSTEEL_CHESTPLATE = ITEMS.register("true_perfect_buddysteel_chestplate", () -> new BuddycardsArmorItem(BuddycardsMisc.TRUE_PERFECT_BUDDYSTEEL_ARMOR, ArmorItem.Type.CHESTPLATE));
    //public static final DeferredItem<Item> TRUE_PERFECT_BUDDYSTEEL_LEGGINGS = ITEMS.register("true_perfect_buddysteel_leggings", () -> new BuddycardsArmorItem(BuddycardsMisc.TRUE_PERFECT_BUDDYSTEEL_ARMOR, ArmorItem.Type.LEGGINGS));
    //public static final DeferredItem<Item> TRUE_PERFECT_BUDDYSTEEL_BOOTS = ITEMS.register("true_perfect_buddysteel_boots", () -> new BuddycardsArmorItem(BuddycardsMisc.TRUE_PERFECT_BUDDYSTEEL_ARMOR, ArmorItem.Type.BOOTS));
    //public static final DeferredItem<Item> TRUE_PERFECT_BUDDYSTEEL_SWORD = ITEMS.register("true_perfect_buddysteel_sword", () -> new SwordItem(BuddycardsToolTier.TRUE_PERFECT_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> TRUE_PERFECT_BUDDYSTEEL_SHOVEL = ITEMS.register("true_perfect_buddysteel_shovel", () -> new ShovelItem(BuddycardsToolTier.TRUE_PERFECT_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> TRUE_PERFECT_BUDDYSTEEL_PICKAXE = ITEMS.register("true_perfect_buddysteel_pickaxe", () -> new PickaxeItem(BuddycardsToolTier.TRUE_PERFECT_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> TRUE_PERFECT_BUDDYSTEEL_AXE = ITEMS.register("true_perfect_buddysteel_axe", () -> new AxeItem(BuddycardsToolTier.TRUE_PERFECT_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    //public static final DeferredItem<Item> TRUE_PERFECT_BUDDYSTEEL_HOE = ITEMS.register("true_perfect_buddysteel_hoe", () -> new HoeItem(BuddycardsToolTier.TRUE_PERFECT_BUDDYSTEEL, RARE_TOOL_PROPERTIES));
    public static final DeferredItem<Item> PERFECT_SCANNER_CHIP = ITEMS.register("perfect_scanner_chip", () -> new DescriptionItem(DEFAULT_PROPERTIES));

    public static final DeferredItem<Item> CREATIVE_SCANNER = ITEMS.register("creative_scanner", () -> new BuddysteelScannerItem(true));
    public static final DeferredItem<GradingSleeveItem> CREATIVE_GRADING_SLEEVE = ITEMS.register("creative_grading_sleeve", () -> new GradingSleeveItem(DEFAULT_EPIC_PROPERTIES, new float[]{0.1f, 0.4f, 0.3f, 0.19f}, true));

    public static final  DeferredItem<SpawnEggItem> ENDERLING_SPAWN_EGG = ITEMS.register("spawn_egg_enderling", () -> new DeferredSpawnEggItem(BuddycardsEntities.ENDERLING, 0x2E2744, 0x9A72CC, DEFAULT_PROPERTIES));

    public static DeferredItem<BuddycardItem> registerCard(BuddycardRequirement requirement, @Nonnull BuddycardSet set, int cardNumber, Rarity rarity) {
        return ITEMS.register("buddycard_" + set.getName() + cardNumber, () -> new BuddycardItem(requirement, set, cardNumber, rarity));
    }
    public static DeferredItem<BuddycardItem> registerCard(@Nonnull BuddycardSet set, int cardNumber, Rarity rarity) {
        return ITEMS.register("buddycard_" + set.getName() + cardNumber, () -> new BuddycardItem(DEFAULT_BUDDYCARD_REQUIREMENT, set, cardNumber, rarity));
    }

    public static void registerCards(BuddycardRequirement requirement, @Nonnull BuddycardSet set, int startingCardNumber, int cardAmt, Rarity rarity) {
        for (int i = startingCardNumber; i < startingCardNumber + cardAmt; i++) {
            registerCard(requirement, set, i, rarity);
        }
    }
    public static void registerCards(@Nonnull BuddycardSet set, int startingCardNumber, int cardAmt, Rarity rarity) {
        for (int i = startingCardNumber; i < startingCardNumber + cardAmt; i++) {
            registerCard(set, i, rarity);
        }
    }

    public static void register18Set(@Nonnull BuddycardSet set) {
        registerCards(set, 1, 7, Rarity.COMMON);
        registerCards(set, 8, 6, Rarity.UNCOMMON);
        registerCards(set, 14, 3, Rarity.RARE);
        registerCards(set, 17, 2, Rarity.EPIC);
    }

    public static void register27Set(@Nonnull BuddycardSet set) {
        registerCards(set, 1, 12, Rarity.COMMON);
        registerCards(set, 13, 9, Rarity.UNCOMMON);
        registerCards(set, 22, 4, Rarity.RARE);
        registerCards(set, 26, 2, Rarity.EPIC);
    }

    public static void register36Set(@Nonnull BuddycardSet set) {
        registerCards(set, 1, 16, Rarity.COMMON);
        registerCards(set, 17, 12, Rarity.UNCOMMON);
        registerCards(set, 29, 6, Rarity.RARE);
        registerCards(set, 35, 2, Rarity.EPIC);
    }

    public interface BuddycardRequirement {
        boolean shouldLoad();
    }
}
