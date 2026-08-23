package com.wildcard.buddycards.integration;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.recipe.BuddysteelChargingRecipe;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import com.wildcard.buddycards.screens.ChargerScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return Buddycards.buddycardsLocation("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BuddysteelChargingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<BuddysteelChargingRecipe> recipes = manager.getAllRecipesFor(BuddycardsMisc.CHARGING_RECIPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(BuddysteelChargingRecipeCategory.TYPE, recipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
            registration.addRecipeClickArea(ChargerScreen.class, 52, 30, 72, 20, BuddysteelChargingRecipeCategory.TYPE);
    }
}
