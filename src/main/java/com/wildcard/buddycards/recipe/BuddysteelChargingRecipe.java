package com.wildcard.buddycards.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wildcard.buddycards.Buddycards;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class BuddysteelChargingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient input;
    private final Ingredient meter;
    private final NonNullList<Ingredient> ingredients;
    private final int powerReq;

    public BuddysteelChargingRecipe(ResourceLocation id, ItemStack output, Ingredient input, Ingredient meter, NonNullList<Ingredient> ingredients, int powerReq) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.meter = meter;
        this.ingredients = ingredients;
        this.powerReq = powerReq;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if(input.test(container.getItem(0)) && meter.test(container.getItem(5))) {
            for (int i = 0; i < ingredients.size(); i++) {
                if(!ingredients.get(i).test(container.getItem(i + 1)))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess access) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int w, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getPowerReq() {
        return powerReq;
    }

    public Ingredient getInput() {
        return input;
    }

    public Ingredient getMeter() {
        return meter;
    }

    public ItemStack getResultItem() {
        return output;
    }

    public static class Type implements RecipeType<BuddysteelChargingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "buddysteel_charging";
    }

    public static class Serializer implements RecipeSerializer<BuddysteelChargingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Buddycards.MOD_ID,"buddysteel_charging");

        @Override
        public BuddysteelChargingRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            Ingredient meter = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "meter"));
            JsonArray ingredientsJson = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.withSize(4, Ingredient.EMPTY);
            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromJson(ingredientsJson.get(i)));
            }
            int req = GsonHelper.getAsInt(json, "power");
            return new BuddysteelChargingRecipe(id, output, input, meter, ingredients, req);
        }

        @Override
        public BuddysteelChargingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buf));
            int req = buf.readInt();
            Ingredient input = Ingredient.fromNetwork(buf);
            Ingredient meter = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();
            return new BuddysteelChargingRecipe(id, output, input, meter, inputs, req);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BuddysteelChargingRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeInt(recipe.powerReq);
            recipe.input.toNetwork(buf);
            recipe.meter.toNetwork(buf);
            buf.writeItemStack(recipe.getResultItem(RegistryAccess.EMPTY), false);
        }

        /**@Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return Serializer.castClass(RecipeSerializer.class);
        }**/

        @SuppressWarnings("unchecked")
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}
