package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.block.*;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BuddycardsBlocks {
    public static final List<Supplier<CardDisplayBlock>> DISPLAY_BLOCKS = new ArrayList<>();
    public static final List<Supplier<CardStandBlock>> STAND_BLOCKS = new ArrayList<>();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Buddycards.MOD_ID);

    public static void registerBlocks(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static final BlockBehaviour.Properties BUDDYSTEEL_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL);
    public static final BlockBehaviour.Properties BOOSTER_BOX_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(0.8F).sound(SoundType.WOOL);
    public static final BlockBehaviour.Properties PLAYMAT_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).strength(0.8F).sound(SoundType.WOOL);

    static final BlockBehaviour.Properties LUMINIS_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().lightLevel((i) -> 12).noOcclusion().strength(3.0F, 6.0F).sound(SoundType.COPPER);
    static final BlockBehaviour.Properties CRYSTAL_LUMINIS_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().lightLevel((i) -> 8).noOcclusion().strength(1.5F, 3.0F).sound(SoundType.AMETHYST);
    static final BlockBehaviour.Properties CRIMSON_LUMINIS_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.CRIMSON_NYLIUM).requiresCorrectToolForDrops().lightLevel((i) -> 8).noOcclusion().strength(3.0F, 6.0F).sound(SoundType.COPPER);
    static final BlockBehaviour.Properties ZYLEX_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL);
    static final BlockBehaviour.Properties VOID_ZYLEX_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_HYPHAE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL);

    public static final BooleanProperty GRADING_PROPERTY = BooleanProperty.create("grading");

    //Basic Blocks
    public static final DeferredBlock<Block> BUDDYSTEEL_BLOCK = BLOCKS.register("buddysteel_block", () -> new Block(BUDDYSTEEL_PROPERTIES));
    public static final DeferredBlock<Block> CHARGED_BUDDYSTEEL_BLOCK = BLOCKS.register("charged_buddysteel_block", () -> new Block(BUDDYSTEEL_PROPERTIES));
    public static final DeferredBlock<Block> PERFECT_BUDDYSTEEL_BLOCK = BLOCKS.register("perfect_buddysteel_block", () -> new Block(BUDDYSTEEL_PROPERTIES));
    public static final DeferredBlock<Block> TRUE_PERFECT_BUDDYSTEEL_BLOCK = BLOCKS.register("true_perfect_buddysteel_block", () -> new Block(BUDDYSTEEL_PROPERTIES));
    public static final DeferredBlock<Block> LUMINIS_ORE = BLOCKS.register("luminis_ore", () -> new DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE).lightLevel((i) -> 3).noOcclusion()));
    public static final DeferredBlock<Block> DEEPSLATE_LUMINIS_ORE = BLOCKS.register("deepslate_luminis_ore", () -> new DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_GOLD_ORE).lightLevel((i) -> 3).noOcclusion()));
    public static final DeferredBlock<Block> LUMINIS_CRYSTAL_BLOCK = BLOCKS.register("luminis_crystal_block", () -> new Block(CRYSTAL_LUMINIS_PROPERTIES));
    public static final DeferredBlock<Block> LUMINIS_BLOCK = BLOCKS.register("luminis_block", () -> new Block(LUMINIS_PROPERTIES));
    public static final DeferredBlock<Block> CRIMSON_LUMINIS_BLOCK = BLOCKS.register("crimson_luminis_block", () -> new Block(CRIMSON_LUMINIS_PROPERTIES));
    public static final DeferredBlock<Block> LUMINIS_PANELS = BLOCKS.register("luminis_panels", () -> new Block(LUMINIS_PROPERTIES));
    public static final DeferredBlock<Block> LUMINIS_PILLAR = BLOCKS.register("luminis_pillar", () -> new RotatedPillarBlock(LUMINIS_PROPERTIES));
    public static final DeferredBlock<Block> LUMINIS_CRYSTAL_PILLAR = BLOCKS.register("luminis_crystal_pillar", () -> new RotatedPillarBlock(LUMINIS_PROPERTIES));
    public static final DeferredBlock<Block> ZYLEX_BLOCK = BLOCKS.register("zylex_block", () -> new Block(ZYLEX_PROPERTIES));
    public static final DeferredBlock<Block> VOID_ZYLEX_BLOCK = BLOCKS.register("void_zylex_block", () -> new Block(VOID_ZYLEX_PROPERTIES));
    //Displays
    public static final DeferredBlock<CardDisplayBlock> OAK_CARD_DISPLAY = registerDisplay("oak_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredBlock<CardDisplayBlock> SPRUCE_CARD_DISPLAY = registerDisplay("spruce_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS)));
    public static final DeferredBlock<CardDisplayBlock> BIRCH_CARD_DISPLAY = registerDisplay("birch_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS)));
    public static final DeferredBlock<CardDisplayBlock> JUNGLE_CARD_DISPLAY = registerDisplay("jungle_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS)));
    public static final DeferredBlock<CardDisplayBlock> ACACIA_CARD_DISPLAY = registerDisplay("acacia_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS)));
    public static final DeferredBlock<CardDisplayBlock> DARK_OAK_CARD_DISPLAY = registerDisplay("dark_oak_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS)));
    public static final DeferredBlock<CardDisplayBlock> CRIMSON_CARD_DISPLAY = registerDisplay("crimson_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_PLANKS)));
    public static final DeferredBlock<CardDisplayBlock> WARPED_CARD_DISPLAY = registerDisplay("warped_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PLANKS)));
    public static final DeferredBlock<CardDisplayBlock> MANGROVE_CARD_DISPLAY = registerDisplay("mangrove_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS)));
    public static final DeferredBlock<CardDisplayBlock> CHERRY_CARD_DISPLAY = registerDisplay("cherry_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS)));
    public static final DeferredBlock<CardDisplayBlock> BAMBOO_CARD_DISPLAY = registerDisplay("bamboo_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS)));
    public static final DeferredBlock<CardDisplayBlock> LUMINIS_CARD_DISPLAY = registerDisplay("luminis_card_display", () -> new CardDisplayBlock(LUMINIS_PROPERTIES));
    //Stands
    public static final DeferredBlock<CardStandBlock> STONE_CARD_STAND = registerStand("stone_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<CardStandBlock> DEEPSLATE_CARD_STAND = registerStand("deepslate_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));
    public static final DeferredBlock<CardStandBlock> BLACKSTONE_CARD_STAND = registerStand("blackstone_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE)));
    public static final DeferredBlock<CardStandBlock> ANDESITE_CARD_STAND = registerStand("andesite_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE)));
    public static final DeferredBlock<CardStandBlock> DIORITE_CARD_STAND = registerStand("diorite_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE)));
    public static final DeferredBlock<CardStandBlock> GRANITE_CARD_STAND = registerStand("granite_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE)));
    public static final DeferredBlock<CardStandBlock> SANDSTONE_CARD_STAND = registerStand("sandstone_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredBlock<CardStandBlock> RED_SANDSTONE_CARD_STAND = registerStand("red_sandstone_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SANDSTONE)));
    public static final DeferredBlock<CardStandBlock> PRISMARINE_CARD_STAND = registerStand("prismarine_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));
    public static final DeferredBlock<CardStandBlock> CALCITE_CARD_STAND = registerStand("calcite_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE)));
    public static final DeferredBlock<CardStandBlock> TUFF_CARD_STAND = registerStand("tuff_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF)));
    public static final DeferredBlock<CardStandBlock> DRIPSTONE_CARD_STAND = registerStand("dripstone_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK)));
    public static final DeferredBlock<CardStandBlock> BASALT_CARD_STAND = registerStand("basalt_card_stand", () -> new CardStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BASALT)));
    //Booster Boxes
    public static final DeferredBlock<Block> BOOSTER_BOX_BASE = BLOCKS.register("buddycard_booster_box_base", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BOOSTER_BOX_PROPERTIES));
    public static final DeferredBlock<Block> BOOSTER_BOX_NETHER = BLOCKS.register("buddycard_booster_box_nether", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BOOSTER_BOX_PROPERTIES));
    public static final DeferredBlock<Block> BOOSTER_BOX_END = BLOCKS.register("buddycard_booster_box_end", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BOOSTER_BOX_PROPERTIES));
    public static final DeferredBlock<Block> BOOSTER_BOX_CAVE = BLOCKS.register("buddycard_booster_box_cave", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BOOSTER_BOX_PROPERTIES));
    public static final DeferredBlock<Block> BOOSTER_BOX_MYSTERY = BLOCKS.register("buddycard_booster_box_mystery", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BOOSTER_BOX_PROPERTIES));
    //Misc
    public static final DeferredBlock<Block> CHARGER = BLOCKS.register("buddysteel_charger", () -> new BuddysteelChargerBlock(BUDDYSTEEL_PROPERTIES));
    public static final DeferredBlock<Block> KINETIC_CHAMBER = BLOCKS.register("kinetic_chamber", () -> new KineticChamberBlock(CRIMSON_LUMINIS_PROPERTIES.explosionResistance(0)));
    public static final DeferredBlock<Block> GRADER = BLOCKS.register("grader", () -> new GraderBlock(ZYLEX_PROPERTIES));

    public static DeferredBlock<CardStandBlock> registerStand(String id, Supplier<CardStandBlock> supplier) {
        DeferredBlock<CardStandBlock> stand = BLOCKS.register(id, supplier);
        STAND_BLOCKS.add(stand);
        return stand;
    }

    public static DeferredBlock<CardDisplayBlock> registerDisplay(String id, Supplier<CardDisplayBlock> supplier) {
        DeferredBlock<CardDisplayBlock> display = BLOCKS.register(id, supplier);
        DISPLAY_BLOCKS.add(display);
        return display;
    }
}
