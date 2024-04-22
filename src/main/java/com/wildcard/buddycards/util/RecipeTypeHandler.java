package com.wildcard.buddycards.util;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.recipe.BuddysteelChargingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Buddycards.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeTypeHandler {
    @SubscribeEvent
    public void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, BuddysteelChargingRecipe.Type.ID, BuddysteelChargingRecipe.Type.INSTANCE);
    }
}
