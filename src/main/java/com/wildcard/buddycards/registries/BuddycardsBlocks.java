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

public class BuddycardsBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Buddycards.MOD_ID);

    public static void registerBlocks() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final BlockBehaviour.Properties BOOSTER_BOX_PROPERTIES = BlockBehaviour.Properties.copy(Blocks.GRAY_WOOL);

    //Basic Blocks
    public static final RegistryObject<Block> BUDDYSTEEL_BLOCK = BLOCKS.register("buddysteel_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK)));
    //Displays
    public static final RegistryObject<Block> OAK_CARD_DISPLAY = BLOCKS.register("oak_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> SPRUCE_CARD_DISPLAY = BLOCKS.register("spruce_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
    public static final RegistryObject<Block> BIRCH_CARD_DISPLAY = BLOCKS.register("birch_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
    public static final RegistryObject<Block> JUNGLE_CARD_DISPLAY = BLOCKS.register("jungle_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
    public static final RegistryObject<Block> ACACIA_CARD_DISPLAY = BLOCKS.register("acacia_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
    public static final RegistryObject<Block> DARK_OAK_CARD_DISPLAY = BLOCKS.register("dark_oak_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
    public static final RegistryObject<Block> CRIMSON_CARD_DISPLAY = BLOCKS.register("crimson_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
    public static final RegistryObject<Block> WARPED_CARD_DISPLAY = BLOCKS.register("warped_card_display", () -> new CardDisplayBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
    //Booster Boxes
    public static final RegistryObject<Block> BOOSTER_BOX_BASE = BuddycardsBlocks.BLOCKS.register("buddycard_booster_box_base", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BuddycardsBlocks.BOOSTER_BOX_PROPERTIES));
    public static final RegistryObject<Block> BOOSTER_BOX_NETHER = BuddycardsBlocks.BLOCKS.register("buddycard_booster_box_nether", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BuddycardsBlocks.BOOSTER_BOX_PROPERTIES));
    public static final RegistryObject<Block> BOOSTER_BOX_END = BuddycardsBlocks.BLOCKS.register("buddycard_booster_box_end", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BuddycardsBlocks.BOOSTER_BOX_PROPERTIES));
    public static final RegistryObject<Block> BOOSTER_BOX_MYSTERY = BuddycardsBlocks.BLOCKS.register("buddycard_booster_box_mystery", () -> new BuddycardBoosterBoxBlock(BuddycardsItems.DEFAULT_BUDDYCARD_REQUIREMENT, BuddycardsBlocks.BOOSTER_BOX_PROPERTIES));

}
