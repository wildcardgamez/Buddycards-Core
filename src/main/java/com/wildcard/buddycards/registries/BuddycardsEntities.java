package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.block.entity.*;
import com.wildcard.buddycards.entity.EnderlingEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BuddycardsEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Buddycards.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Buddycards.MOD_ID);

    public static void registerEntities(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
        ENTITIES.register(eventBus);
    }

    public static final Supplier<BlockEntityType<CardDisplayBlockEntity>> CARD_DISPLAY_ENTITY = BLOCK_ENTITIES.register("card_display",
            () -> {
                Block[] blocks = BuddycardsBlocks.DISPLAY_BLOCKS.stream().map(Supplier::get).toArray(Block[]::new);
                return BlockEntityType.Builder.of(CardDisplayBlockEntity::new, blocks).build(null);
            });
    public static final Supplier<BlockEntityType<CardStandBlockEntity>> CARD_STAND_ENTITY = BLOCK_ENTITIES.register("card_stand",
            () -> {
                Block[] blocks = BuddycardsBlocks.STAND_BLOCKS.stream().map(Supplier::get).toArray(Block[]::new);
                return BlockEntityType.Builder.of(CardStandBlockEntity::new, blocks).build(null);
            });
    public static final Supplier<BlockEntityType<BuddysteelChargerBlockEntity>> CHARGER_ENTITY = BLOCK_ENTITIES.register("buddysteel_charger",
            () -> BlockEntityType.Builder.of(BuddysteelChargerBlockEntity::new, BuddycardsBlocks.CHARGER.get()).build(null));
    public static final Supplier<BlockEntityType<GraderBlockEntity>> GRADER_ENTITY = BLOCK_ENTITIES.register("grader",
            () -> BlockEntityType.Builder.of(GraderBlockEntity::new, BuddycardsBlocks.GRADER.get()).build(null));
    public static final Supplier<BlockEntityType<KineticChamberBlockEntity>> KINETIC_CHAMBER_ENTITY = BLOCK_ENTITIES.register("kinetic_chamber",
            () -> BlockEntityType.Builder.of(KineticChamberBlockEntity::new, BuddycardsBlocks.KINETIC_CHAMBER.get()).build(null));

    public static final Supplier<EntityType<EnderlingEntity>> ENDERLING = ENTITIES.register("enderling",
            () -> EntityType.Builder.of(EnderlingEntity::new, MobCategory.CREATURE).sized(.6f, 1.8f).build(ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "enderling").toString()));
}