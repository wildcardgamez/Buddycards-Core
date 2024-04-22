package com.wildcard.buddycards.integration;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.recipe.BuddysteelChargingRecipe;
import com.wildcard.buddycards.registries.BuddycardsBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class BuddysteelChargingRecipeCategory implements IRecipeCategory<BuddysteelChargingRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Buddycards.MOD_ID, "buddysteel_charging");
    public final static ResourceLocation TEXTURE = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/buddysteel_charger.png");

    private final IDrawable background;
    private final IDrawable icon;

    public BuddysteelChargingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 25, 17, 126, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(BuddycardsBlocks.BUDDYSTEEL_CHARGER.get()));
    }

    @Override
    public Component getTitle() {
        return BuddycardsBlocks.BUDDYSTEEL_CHARGER.get().getName();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends BuddysteelChargingRecipe> getRecipeClass() {
        return BuddysteelChargingRecipe.class;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BuddysteelChargingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 19).addIngredients(recipe.getInput());
        for (int i = 0; i < recipe.getIngredients().size(); i++)
            builder.addSlot(RecipeIngredientRole.INPUT, 28 + i * 18, 1).addIngredients(recipe.getIngredients().get(i));
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 37).addIngredients(recipe.getMeter());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 19).addItemStack(recipe.getResultItem());
    }
}
