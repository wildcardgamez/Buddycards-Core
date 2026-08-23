package com.wildcard.buddycards.integration;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.recipe.BuddysteelChargingRecipe;
import com.wildcard.buddycards.registries.BuddycardsBlocks;
import com.wildcard.buddycards.registries.BuddycardsItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class BuddysteelChargingRecipeCategory implements IRecipeCategory<BuddysteelChargingRecipe> {
    public final static ResourceLocation TEXTURE = Buddycards.buddycardsLocation("textures/gui/buddysteel_charger.png");
    public final static RecipeType<BuddysteelChargingRecipe> TYPE = new RecipeType<>(Buddycards.buddycardsLocation("buddysteel_charging"), BuddysteelChargingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public BuddysteelChargingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 25, 17, 126, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BuddycardsBlocks.CHARGER.get()));
    }

    @Override
    public RecipeType<BuddysteelChargingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.buddycards.buddysteel_charger");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BuddysteelChargingRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(1, 19).addIngredients(recipe.getInput());
        for (int i = 0; i < recipe.getIngredients().size(); i++)
            builder.addInputSlot(28 + i * 18, 1).addIngredients(recipe.getIngredients().get(i));
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 37).addIngredients(Ingredient.of(new ItemStack(BuddycardsItems.BUDDYSTEEL_SCANNER.get())))
                .addRichTooltipCallback((iRecipeSlotView, iTooltipBuilder) -> iTooltipBuilder.add(Component.translatable("block.buddycards.buddysteel_charger.jei_require")
                        .append((int)(recipe.getPercentage() * 100) + "%").append(Component.translatable("block.buddycards.buddysteel_charger.jei_tier" + recipe.getTier()))
                        .append(Component.translatable("item.buddycards.buddycard.set_" + recipe.getSet()))));
        builder.addOutputSlot(109, 19).addItemStack(recipe.getResultItem());
    }
}
