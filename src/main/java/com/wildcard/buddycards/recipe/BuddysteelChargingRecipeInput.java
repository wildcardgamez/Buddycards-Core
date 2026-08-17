package com.wildcard.buddycards.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record BuddysteelChargingRecipeInput(ItemStack input, NonNullList<ItemStack> ingredients) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        if (i == 0)
            return input();
        else
            return ingredients().get(i-1);
    }

    @Override
    public int size() {
        return 5;
    }
}
