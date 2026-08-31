package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.ICollectionTieredItem;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.menu.*;
import com.wildcard.buddycards.recipe.BuddysteelChargingRecipe;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class BuddycardsMisc {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, Buddycards.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Buddycards.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(Registries.RECIPE_TYPE, Buddycards.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Buddycards.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Buddycards.MOD_ID);

    public static void registerStuff(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
        MENUS.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPES.register(eventBus);
        TABS.register(eventBus);
    }

    //ARMOR MATERIALS
    public static final Holder<ArmorMaterial> BUDDYSTEEL_ARMOR = registerArmorMaterial("buddysteel",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }),
            SoundEvents.ARMOR_EQUIP_IRON,
            12, 1, 0, BuddycardsItems.BUDDYSTEEL_INGOT);
    public static final Holder<ArmorMaterial> LUMINIS_ARMOR = registerArmorMaterial("luminis",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }),
            SoundEvents.ARMOR_EQUIP_IRON,
            12, 1, 0, BuddycardsItems.CRIMSON_LUMINIS);
    public static final Holder<ArmorMaterial> ZYLEX_ARMOR = registerArmorMaterial("zylex",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }),
            SoundEvents.ARMOR_EQUIP_IRON,
            12, 1, 0, BuddycardsItems.VOID_ZYLEX);
    public static final Holder<ArmorMaterial> CHARGED_BUDDYSTEEL_ARMOR = registerArmorMaterial("charged_buddysteel",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }),
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            13, 2, 0, BuddycardsItems.CHARGED_BUDDYSTEEL_INGOT);
    public static final Holder<ArmorMaterial> MIXED_BUDDYSTEEL_ARMOR = registerArmorMaterial("mixed_buddysteel",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }),
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            13, 3, 0f, BuddycardsItems.CHARGED_BUDDYSTEEL_INGOT);
    public static final Holder<ArmorMaterial> PERFECT_BUDDYSTEEL_ARMOR = registerArmorMaterial("perfect_buddysteel",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }),
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            15, 3, 0.05f, BuddycardsItems.PERFECT_BUDDYSTEEL_INGOT);
    public static final Holder<ArmorMaterial> TRUE_PERFECT_BUDDYSTEEL_ARMOR = registerArmorMaterial("true_perfect_buddysteel",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 4);
                map.put(ArmorItem.Type.LEGGINGS, 7);
                map.put(ArmorItem.Type.CHESTPLATE, 10);
                map.put(ArmorItem.Type.HELMET, 4);
                map.put(ArmorItem.Type.BODY, 13);
            }),
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            15, 3, 0.1f, BuddycardsItems.TRUE_PERFECT_BUDDYSTEEL_INGOT);

    //Menus
    public static final DeferredHolder<MenuType<?>, MenuType<BinderMenu>> BINDER_MENU = MENUS.register("binder", () -> new MenuType<>(BinderMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<ScannerMenu>> SCANNER_MENU = MENUS.register("scanner", () -> new MenuType<>(ScannerMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<ChargerMenu>> CHARGER_MENU = MENUS.register("buddysteel_charger", () -> IMenuTypeExtension.create(ChargerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<GraderMenu>> GRADER_MENU = MENUS.register("grader", () -> IMenuTypeExtension.create(GraderMenu::new));

    //TABS
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = TABS.register("buddycards_items", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.buddycards"))
            .icon(() -> BuddycardsItems.PACK_BASE.get().asItem().getDefaultInstance())
            .displayItems((a, b) -> {
                for (DeferredHolder<Item, ? extends Item> i : BuddycardsItems.ITEMS.getEntries()) {
                    if (!(i.get() instanceof BuddycardItem))
                        b.accept(i.get());
                }
            })
            .build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CARDS_TAB = TABS.register("buddycards", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.buddycards_cards"))
            .icon(() -> BuddycardsItems.PACK_BASE.get().rollCard(RandomSource.create()).getDefaultInstance())
            .displayItems((a, b) -> {
                for (DeferredHolder<Item, ? extends Item> i : BuddycardsItems.ITEMS.getEntries()) {
                    if (i.get() instanceof BuddycardItem && ((BuddycardItem) i.get()).shouldLoad())
                        b.accept(i.get());
                }
            })
            .build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GEAR_TAB = TABS.register("buddycards_gear", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.buddycards_gear"))
            .icon(() -> BuddycardsItems.CHARGER.get().getDefaultInstance())
            .displayItems((a, b) -> {
                for (DeferredHolder<Item, ? extends Item> i : BuddycardsItems.ITEMS.getEntries()) {
                    if (i.get() instanceof ICollectionTieredItem)
                        for (int j = 0; j < 5; j++) {
                            ItemStack stack = i.get().getDefaultInstance();
                            stack.set(BuddycardsComponents.COLLECTION_TIER, j);
                            b.accept(stack);
                        }
                }
            })
            .build());

    //RECIPES
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BuddysteelChargingRecipe>> CHARGING_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("buddysteel_charging", BuddysteelChargingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<BuddysteelChargingRecipe>> CHARGING_RECIPE =
            RECIPES.register("buddysteel_charging", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "buddysteel_charging";
                }
            });

    private static Holder<ArmorMaterial> registerArmorMaterial(String name, EnumMap<ArmorItem.Type, Integer> typeProtection, Holder<SoundEvent> equipSound, int enchantability, float toughness, float knockbackResistance, Supplier<Item> ingredientItem) {
        ResourceLocation location = Buddycards.buddycardsLocation(name);
        Supplier<Ingredient> ingredient = () -> Ingredient.of(ingredientItem.get());
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(location));

        EnumMap<ArmorItem.Type, Integer> typeMap = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            typeMap.put(type, typeProtection.get(type));
        }

        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(typeProtection, enchantability, equipSound, ingredient, layers, toughness, knockbackResistance));
    }
}
