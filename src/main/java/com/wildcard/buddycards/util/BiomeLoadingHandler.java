package com.wildcard.buddycards.util;

import com.wildcard.buddycards.registries.BuddycardsFeatures;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BiomeLoadingHandler {
    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event) {
        ResourceLocation name = event.getName();
        if(name != null && BiomeDictionary.getTypes(ResourceKey.create(Registry.BIOME_REGISTRY, name)).contains(BiomeDictionary.Type.OVERWORLD)) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.RAW_GENERATION).add(BuddycardsFeatures.PLACED_LUMINIS_VEIN.getHolder().get());
        }
    }
}
