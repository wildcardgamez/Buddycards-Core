package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.block.entity.CardDisplayBlockEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BuddycardsEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Buddycards.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Buddycards.MOD_ID);

    public static void registerEntities() {
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BlockEntityType<CardDisplayBlockEntity>> CARD_DISPLAY_TILE = TILE_ENTITIES.register("card_display",
            () -> BlockEntityType.Builder.of(CardDisplayBlockEntity::new, BuddycardsBlocks.OAK_CARD_DISPLAY.get(), BuddycardsBlocks.SPRUCE_CARD_DISPLAY.get(),
                    BuddycardsBlocks.BIRCH_CARD_DISPLAY.get(), BuddycardsBlocks.JUNGLE_CARD_DISPLAY.get(), BuddycardsBlocks.ACACIA_CARD_DISPLAY.get(), BuddycardsBlocks.DARK_OAK_CARD_DISPLAY.get(),
                    BuddycardsBlocks.CRIMSON_CARD_DISPLAY.get(), BuddycardsBlocks.WARPED_CARD_DISPLAY.get()).build(null));

}
