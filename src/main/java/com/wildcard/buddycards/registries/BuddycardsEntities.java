package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.block.entity.BuddysteelChargerBlockEntity;
import com.wildcard.buddycards.block.entity.PlaymatBlockEntity;
import com.wildcard.buddycards.block.entity.CardDisplayBlockEntity;
import com.wildcard.buddycards.block.entity.KineticChamberBlockEntity;
import com.wildcard.buddycards.entity.EnderlingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BuddycardsEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Buddycards.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Buddycards.MOD_ID);

    public static void registerEntities() {
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BlockEntityType<CardDisplayBlockEntity>> CARD_DISPLAY_ENTITY = BLOCK_ENTITIES.register("card_display",
            () -> {
                Block[] blocks = BuddycardsBlocks.DISPLAY_BLOCKS.stream().map(Supplier::get).toArray(Block[]::new);
                return BlockEntityType.Builder.of(CardDisplayBlockEntity::new, blocks).build(null);
            });
    public static final RegistryObject<BlockEntityType<PlaymatBlockEntity>> PLAYMAT_ENTITY = BLOCK_ENTITIES.register("battle_board",
            () -> {
                Block[] blocks = BuddycardsBlocks.PLAYMAT_BLOCKS.stream().map(Supplier::get).toArray(Block[]::new);
                return BlockEntityType.Builder.of(PlaymatBlockEntity::new, blocks).build(null);
            });
    public static final RegistryObject<BlockEntityType<KineticChamberBlockEntity>> KINETIC_CHAMBER_TILE = BLOCK_ENTITIES.register("kinetic_chamber",
            () -> BlockEntityType.Builder.of(KineticChamberBlockEntity::new, BuddycardsBlocks.KINETIC_CHAMBER.get()).build(null));
    public static final RegistryObject<BlockEntityType<BuddysteelChargerBlockEntity>> BUDDYSTEEL_CHARGER_TILE = BLOCK_ENTITIES.register("buddysteel_charger",
            () -> BlockEntityType.Builder.of(BuddysteelChargerBlockEntity::new, BuddycardsBlocks.BUDDYSTEEL_CHARGER.get()).build(null));

    public static final RegistryObject<EntityType<EnderlingEntity>> ENDERLING = ENTITIES.register("enderling",
            () -> EntityType.Builder.of(EnderlingEntity::new, MobCategory.CREATURE).sized(.6f, 1.8f).build(new ResourceLocation(Buddycards.MOD_ID, "enderling").toString()));
}
