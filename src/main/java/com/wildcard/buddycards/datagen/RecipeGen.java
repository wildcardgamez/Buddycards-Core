package com.wildcard.buddycards.datagen;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.recipe.BuddysteelChargingRecipe;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

import static com.wildcard.buddycards.registries.BuddycardsItems.*;

public class RecipeGen extends VanillaRecipeProvider {
    public RecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        generateMedalRecipes(BASE_SET, recipeOutput);
        generateMedalRecipes(NETHER_SET, recipeOutput);
        generateMedalRecipes(END_SET, recipeOutput);
        generateMedalRecipes(CAVE_SET, recipeOutput);
        generateTieredBuddysteelRecipes("charged_buddysteel_helmet", BUDDYSTEEL_HELMET.toStack(), CHARGED_BUDDYSTEEL_HELMET.toStack(), recipeOutput);
        generateTieredBuddysteelRecipes("charged_buddysteel_chestplate", BUDDYSTEEL_CHESTPLATE.toStack(), CHARGED_BUDDYSTEEL_CHESTPLATE.toStack(), recipeOutput);
        generateTieredBuddysteelRecipes("charged_buddysteel_leggings", BUDDYSTEEL_LEGGINGS.toStack(), CHARGED_BUDDYSTEEL_LEGGINGS.toStack(), recipeOutput);
        generateTieredBuddysteelRecipes("charged_buddysteel_boots", BUDDYSTEEL_BOOTS.toStack(), CHARGED_BUDDYSTEEL_BOOTS.toStack(), recipeOutput);
    }

    static void generateMedalRecipes(BuddycardSet set, RecipeOutput recipeOutput) {
        ItemStack medal = set.getMedal().getDefaultInstance();
        recipeOutput.accept(Buddycards.buddycardsLocation("buddysteel_medal_" + set.getName()),
                new BuddysteelChargingRecipe(medal, ingredientOf(BLANK_BUDDYSTEEL_MEDAL.get()),
                        sameIngredient(Ingredient.of(TagKey.create(Registries.ITEM, Buddycards.buddycardsLocation("buddycards_" + set.getName())))),
                        0, 1, set.getName()), null);
        recipeOutput.accept(Buddycards.buddycardsLocation("buddysteel_medal_" + set.getName() + "1"),
                new BuddysteelChargingRecipe(itemCopyWithTier(medal, 1), Ingredient.of(medal),
                        doubleIngredients(ingredientOf(LUMINIS.get()), ingredientOf(CRIMSON_LUMINIS.get())),
                        1, 1, set.getName()), null);
        recipeOutput.accept(Buddycards.buddycardsLocation("buddysteel_medal_" + set.getName() + "2"),
                new BuddysteelChargingRecipe(itemCopyWithTier(medal, 2), Ingredient.of(medal),
                        doubleIngredients(ingredientOf(VOID_ZYLEX.get()), ingredientOf(ZYLEX.get())),
                        2, 1, set.getName()), null);
        recipeOutput.accept(Buddycards.buddycardsLocation("buddysteel_medal_" + set.getName() + "3"),
                new BuddysteelChargingRecipe(itemCopyWithTier(medal, 3), Ingredient.of(medal),
                        doubleIngredients(ingredientOf(CRIMSON_LUMINIS.get()), ingredientOf(VOID_ZYLEX.get())),
                        3, 1, set.getName()), null);
        recipeOutput.accept(Buddycards.buddycardsLocation("buddysteel_medal_" + set.getName() + "4"),
                new BuddysteelChargingRecipe(itemCopyWithTier(medal, 4), Ingredient.of(medal),
                        doubleIngredients(ingredientOf(CRIMSON_LUMINIS_BLOCK.get()), ingredientOf(VOID_ZYLEX_BLOCK.get())),
                        4, 1, set.getName()), null);
    }

    static void generateTieredBuddysteelRecipes(String name, ItemStack basic, ItemStack charged, RecipeOutput recipeOutput) {
        recipeOutput.accept(Buddycards.buddycardsLocation(name),
                new BuddysteelChargingRecipe(charged, Ingredient.of(basic),
                        doubleIngredients(ingredientOf(LUMINIS.get()), ingredientOf(ZYLEX.get())),
                        0, 1, "all"), null);
        recipeOutput.accept(Buddycards.buddycardsLocation(name + "1"),
                new BuddysteelChargingRecipe(itemCopyWithTier(charged, 1), Ingredient.of(charged),
                        doubleIngredients(ingredientOf(LUMINIS.get()), ingredientOf(CRIMSON_LUMINIS.get())),
                        1, 1, "all"), null);
        recipeOutput.accept(Buddycards.buddycardsLocation(name + "2"),
                new BuddysteelChargingRecipe(itemCopyWithTier(charged, 2), Ingredient.of(charged),
                        doubleIngredients(ingredientOf(VOID_ZYLEX.get()), ingredientOf(ZYLEX.get())),
                        2, 1, "all"), null);
        recipeOutput.accept(Buddycards.buddycardsLocation(name + "3"),
                new BuddysteelChargingRecipe(itemCopyWithTier(charged, 3), Ingredient.of(charged),
                        doubleIngredients(ingredientOf(CRIMSON_LUMINIS.get()), ingredientOf(VOID_ZYLEX.get())),
                        3, 1, "all"), null);
        recipeOutput.accept(Buddycards.buddycardsLocation(name + "4"),
                new BuddysteelChargingRecipe(itemCopyWithTier(charged, 4), Ingredient.of(charged),
                        sameIngredient(ingredientOf(TRUE_PERFECT_BUDDYSTEEL_INGOT.get())),
                        4, 1, "all"), null);
    }

    static ItemStack itemCopyWithTier(ItemStack stack, int tier) {
        stack = stack.copy();
        stack.set(BuddycardsComponents.COLLECTION_TIER, tier);
        return stack;
    }

    static Ingredient ingredientOf(Item item) {
        return Ingredient.of(item.getDefaultInstance());
    }

    static NonNullList<Ingredient> doubleIngredients(Ingredient ingredient1, Ingredient ingredient2) {
        return NonNullList.of(ingredient1, ingredient1, ingredient2, ingredient1, ingredient2);
    }

    static NonNullList<Ingredient> sameIngredient(Ingredient ingredient) {
        return NonNullList.withSize(4, ingredient);
    }
}