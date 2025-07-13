package com.wildcard.buddycards.registries;

import com.mojang.serialization.Codec;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.enchantment.EnchantmentBuddyBoost;
import com.wildcard.buddycards.enchantment.EnchantmentExtraPage;
import com.wildcard.buddycards.enchantment.EnchantmentRecovery;
import com.wildcard.buddycards.enchantment.EnchantmentThickPockets;
import com.wildcard.buddycards.item.BuddycardBinderItem;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.loot.LootInjectionModifier;
import com.wildcard.buddycards.menu.BinderMenu;
import com.wildcard.buddycards.menu.ChargerMenu;
import com.wildcard.buddycards.menu.DeckboxMenu;
import com.wildcard.buddycards.menu.PlaymatMenu;
import com.wildcard.buddycards.recipe.BuddysteelChargingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BuddycardsMisc {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Buddycards.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Buddycards.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Buddycards.MOD_ID);
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLMS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Buddycards.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Buddycards.MOD_ID);

    public static void registerStuff() {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RECIPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        GLMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Enchantment
    public static final RegistryObject<Enchantment> EXTRA_PAGE = ENCHANTMENTS.register("extra_page", EnchantmentExtraPage::new);
    public static final RegistryObject<Enchantment> BUDDY_BOOST = ENCHANTMENTS.register("buddy_boost", EnchantmentBuddyBoost::new);
    public static final RegistryObject<Enchantment> RECOVERY = ENCHANTMENTS.register("recovery", EnchantmentRecovery::new);

    public static final EnchantmentCategory BUDDYCARD_BINDER = EnchantmentCategory.create("BUDDY_BINDER", BuddycardBinderItem.class::isInstance);

    //Containers
    public static final RegistryObject<MenuType<BinderMenu>> BINDER_MENU = MENUS.register("binder",
            () -> new MenuType<>(BinderMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final RegistryObject<MenuType<DeckboxMenu>> DECKBOX_CONTAINER = MENUS.register("deckbox",
            () -> new MenuType<>(DeckboxMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final RegistryObject<MenuType<PlaymatMenu>> PLAYMAT_CONTAINER = MENUS.register("playmat",
            () -> IForgeMenuType.create((PlaymatMenu::new)));
    public static final RegistryObject<MenuType<ChargerMenu>> CHARGER_CONTAINER = MENUS.register("buddysteel_charger",
            () -> IForgeMenuType.create((ChargerMenu::new)));

    //Recipes
    public static final RegistryObject<RecipeSerializer<BuddysteelChargingRecipe>> BUDDYSTEEL_CHARGING_SERIALIZER = RECIPES.register("buddysteel_charging", () -> BuddysteelChargingRecipe.Serializer.INSTANCE);

    //GLMs
    public static RegistryObject<Codec<? extends IGlobalLootModifier>> LOOT_INJECTION = GLMS.register("loot_injection", LootInjectionModifier.SERIALIZER);

    //Tags
    public static final TagKey<Item> BCB_ANIMAL = TagKey.create(Registries.ITEM, new ResourceLocation(Buddycards.MOD_ID + ":battles/animal"));
    public static final TagKey<Item> BCB_ENCHANTABLE = TagKey.create(Registries.ITEM, new ResourceLocation(Buddycards.MOD_ID + ":battles/enchantable"));
    public static final TagKey<Item> BCB_FIRE = TagKey.create(Registries.ITEM, new ResourceLocation(Buddycards.MOD_ID + ":battles/fire"));
    public static final TagKey<Item> BCB_FOOD = TagKey.create(Registries.ITEM, new ResourceLocation(Buddycards.MOD_ID + ":battles/food"));
    public static final TagKey<Item> BCB_FUNGAL = TagKey.create(Registries.ITEM, new ResourceLocation(Buddycards.MOD_ID + ":battles/fungal"));
    public static final TagKey<Item> BCB_METAL = TagKey.create(Registries.ITEM, new ResourceLocation(Buddycards.MOD_ID + ":battles/metal"));
    public static final TagKey<Item> BCB_MONSTER = TagKey.create(Registries.ITEM, new ResourceLocation(Buddycards.MOD_ID + ":battles/monster"));
    public static final TagKey<Item> BCB_REDSTONE = TagKey.create(Registries.ITEM, new ResourceLocation(Buddycards.MOD_ID + ":battles/redstone"));

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = TABS.register("buddycards_items", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Buddycards.MOD_ID))
            .icon(() -> BuddycardsItems.PACK_BASE.get().asItem().getDefaultInstance())
            .displayItems((a, b) -> {
                for (RegistryObject<Item> i: BuddycardsItems.ITEMS.getEntries()) {
                    if(!(i.get() instanceof BuddycardItem))
                        b.accept(i.get());
                }
            })
            .build());

    public static final RegistryObject<CreativeModeTab> CARDS_TAB = TABS.register("buddycards", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Buddycards.MOD_ID))
            .icon(() -> BuddycardsItems.PACK_BASE.get().rollCard(RandomSource.create()).getDefaultInstance())
            .displayItems((a, b) -> {
                for (RegistryObject<Item> i: BuddycardsItems.ITEMS.getEntries()) {
                    if(i.get() instanceof BuddycardItem && ((BuddycardItem) i.get()).shouldLoad())
                        b.accept(i.get());
                }
            })
            .build());
}
