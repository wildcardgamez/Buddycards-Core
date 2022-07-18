package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.block.entity.CardDisplayBlockEntity;
import com.wildcard.buddycards.block.entity.KineticChamberBlockEntity;
import com.wildcard.buddycards.entity.EnderlingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BuddycardsEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Buddycards.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Buddycards.MOD_ID);

    public static void registerEntities() {
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BlockEntityType<CardDisplayBlockEntity>> CARD_DISPLAY_TILE = TILE_ENTITIES.register("card_display",
            () -> {
                Block[] blocks = BuddycardsBlocks.DISPLAY_BLOCKS.stream().map(Supplier::get).toArray(Block[]::new);
                return BlockEntityType.Builder.of(CardDisplayBlockEntity::new, blocks).build(null);
            });
    public static final RegistryObject<BlockEntityType<KineticChamberBlockEntity>> KINETIC_CHAMBER_TILE = TILE_ENTITIES.register("kinetic_chamber",
            () -> BlockEntityType.Builder.of(KineticChamberBlockEntity::new, BuddycardsBlocks.KINETIC_CHAMBER.get()).build(null));

    public static RegistryObject<EntityType<EnderlingEntity>> ENDERLING = ENTITIES.register("enderling",
            () -> EntityType.Builder.of(EnderlingEntity::new, MobCategory.CREATURE).sized(.6f, 1.8f).build(new ResourceLocation(Buddycards.MOD_ID, "enderling").toString()));
}
