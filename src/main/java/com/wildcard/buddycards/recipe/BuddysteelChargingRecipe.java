package com.wildcard.buddycards.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class BuddysteelChargingRecipe implements Recipe<BuddysteelChargingRecipeInput> {
    private final ItemStack output;
    private final Ingredient input;
    private final NonNullList<Ingredient> ingredients;
    private final int tier;
    private final float percentage;
    private final String set;

    public BuddysteelChargingRecipe(ItemStack output, Ingredient input, NonNullList<Ingredient> ingredients, int tier, float percentage, String set) {
        this.output = output;
        this.input = input;
        this.ingredients = ingredients;
        this.tier = tier;
        this.percentage = percentage;
        this.set = set;
    }

    @Override
    public boolean matches(BuddysteelChargingRecipeInput recipeInput, Level level) {
        if(input.test(recipeInput.getItem(0))) {
            for (int i = 0; i < ingredients.size(); i++)
                if(!ingredients.get(i).test(recipeInput.getItem(i + 1)))
                    return false;
            return true;
        }
        return false;
    }

    @Override
    public ItemStack assemble(BuddysteelChargingRecipeInput recipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BuddycardsMisc.CHARGING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return BuddycardsMisc.CHARGING_RECIPE.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getResultItem() {
        return output.copy();
    }

    public int getTier() {
        return tier;
    }

    public float getPercentage() {
        return percentage;
    }

    public String getSet() {
        return set;
    }

    public static class Serializer implements RecipeSerializer<BuddysteelChargingRecipe> {
        public static final MapCodec<BuddysteelChargingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                ItemStack.STRICT_CODEC.fieldOf("output").forGetter(BuddysteelChargingRecipe::getResultItem),
                Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(BuddysteelChargingRecipe::getInput),
                Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").xmap(ingredients -> {
                    NonNullList<Ingredient> nonNullList = NonNullList.create();
                    nonNullList.addAll(ingredients);
                    return nonNullList;
                }, ingredients -> ingredients).forGetter(BuddysteelChargingRecipe::getIngredients),
                Codec.INT.optionalFieldOf("tier", 0).forGetter(BuddysteelChargingRecipe::getTier),
                Codec.FLOAT.optionalFieldOf("percentage", 1f).forGetter(BuddysteelChargingRecipe::getPercentage),
                Codec.STRING.optionalFieldOf("set", "all").forGetter(BuddysteelChargingRecipe::getSet)
        ).apply(i, BuddysteelChargingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BuddysteelChargingRecipe> STREAM_CODEC = StreamCodec.of(BuddysteelChargingRecipe.Serializer::toNetwork, BuddysteelChargingRecipe.Serializer::fromNetwork);

        @Override
        public MapCodec<BuddysteelChargingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BuddysteelChargingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static BuddysteelChargingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            int i = buf.readInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < i; j++) {
                ingredients.set(j, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }
            int tier = buf.readInt();
            float percentage = buf.readFloat();
            String set = buf.readUtf();
            return new BuddysteelChargingRecipe(output, input, ingredients, tier, percentage, set);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buf, BuddysteelChargingRecipe recipe) {
            ItemStack.STREAM_CODEC.encode(buf, recipe.getResultItem());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getInput());
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing);
            }
            buf.writeInt(recipe.getTier());
            buf.writeFloat(recipe.getPercentage());
            buf.writeUtf(recipe.getSet());
        }
    }
}