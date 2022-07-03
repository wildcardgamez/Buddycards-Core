package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.worldgen.LuminisVeinFeature;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

public class BuddycardsFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Buddycards.MOD_ID);
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Buddycards.MOD_ID);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Buddycards.MOD_ID);

    public static void registerFeatures() {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONFIGURED_FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PLACED_FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Features
    public static final RegistryObject<LuminisVeinFeature> LUMINIS_VEIN = FEATURES.register("luminis_vein", () ->
            new LuminisVeinFeature(NoneFeatureConfiguration.CODEC));

    //Configured Features
    public static final RegistryObject<ConfiguredFeature<?,?>> CONFIGURED_LUMINIS_VEIN = CONFIGURED_FEATURES.register("configured_luminis_vein", () ->
            new ConfiguredFeature<>(LUMINIS_VEIN.get(), FeatureConfiguration.NONE));

    //Placed Features
    public static final RegistryObject<PlacedFeature> PLACED_LUMINIS_VEIN = PLACED_FEATURES.register("placed_luminis_vein", () ->
            new PlacedFeature(CONFIGURED_LUMINIS_VEIN.getHolder().get(), new ArrayList<>()));

}
