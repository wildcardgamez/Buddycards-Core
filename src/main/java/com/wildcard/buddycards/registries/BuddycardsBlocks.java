package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.block.BuddycardBoosterBoxBlock;
import com.wildcard.buddycards.block.CardDisplayBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BuddycardsBlocks {
    public static final List<Supplier<CardDisplayBlock>> DISPLAY_BLOCKS = new ArrayList<>();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Buddycards.MOD_ID);

    public static void registerBlocks() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final BlockBehaviour.Properties BOOSTER_BOX_PROPERTIES = BlockBehaviour.Properties.copy(Blocks.GRAY_WOOL);

    //Basic Blocks
    public static final RegistryObject<Block> BUDDYSTEEL_BLOCK = BLOCKS.register("buddysteel_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK)));
    //Displays
    public static final RegistryObject<CardDisplayBlock> OAK_CARD_DISPLAY = registerDisplay("oak_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<CardDisplayBlock> SPRUCE_CARD_DISPLAY = registerDisplay("spruce_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
    public static final RegistryObject<CardDisplayBlock> BIRCH_CARD_DISPLAY = registerDisplay("birch_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
    public static final RegistryObject<CardDisplayBlock> JUNGLE_CARD_DISPLAY = registerDisplay("jungle_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
    public static final RegistryObject<CardDisplayBlock> ACACIA_CARD_DISPLAY = registerDisplay("acacia_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
    public static final RegistryObject<CardDisplayBlock> DARK_OAK_CARD_DISPLAY = registerDisplay("dark_oak_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
    public static final RegistryObject<CardDisplayBlock> CRIMSON_CARD_DISPLAY = registerDisplay("crimson_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
    public static final RegistryObject<CardDisplayBlock> WARPED_CARD_DISPLAY = registerDisplay("warped_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
    //Booster Boxes
    public static final RegistryObject<Block> BOOSTER_BOX_BASE = BuddycardsBlocks.BLOCKS.register("buddycard_booster_box_base", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BuddycardsBlocks.BOOSTER_BOX_PROPERTIES));
    public static final RegistryObject<Block> BOOSTER_BOX_NETHER = BuddycardsBlocks.BLOCKS.register("buddycard_booster_box_nether", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BuddycardsBlocks.BOOSTER_BOX_PROPERTIES));
    public static final RegistryObject<Block> BOOSTER_BOX_END = BuddycardsBlocks.BLOCKS.register("buddycard_booster_box_end", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BuddycardsBlocks.BOOSTER_BOX_PROPERTIES));
    public static final RegistryObject<Block> BOOSTER_BOX_MYSTERY = BuddycardsBlocks.BLOCKS.register("buddycard_booster_box_mystery", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BuddycardsBlocks.BOOSTER_BOX_PROPERTIES));

    public static RegistryObject<CardDisplayBlock> registerDisplay(String id, Supplier<CardDisplayBlock> supplier) {
        RegistryObject<CardDisplayBlock> display = BLOCKS.register(id, supplier);
        DISPLAY_BLOCKS.add(display);
        return display;
    }
}
