package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.enchantment.EnchantmentBuddyBoost;
import com.wildcard.buddycards.enchantment.EnchantmentExtraPage;
import com.wildcard.buddycards.enchantment.EnchantmentRecovery;
import com.wildcard.buddycards.menu.BinderMenu;
import com.wildcard.buddycards.menu.DeckboxMenu;
import com.wildcard.buddycards.menu.PlaymatMenu;
import com.wildcard.buddycards.util.LootInjection;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BuddycardsMisc {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Buddycards.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Buddycards.MOD_ID);
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLMS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, Buddycards.MOD_ID);

    public static void registerStuff() {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());
        GLMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Enchantment
    public static final RegistryObject<Enchantment> EXTRA_PAGE = ENCHANTMENTS.register("extra_page", EnchantmentExtraPage::new);
    public static final RegistryObject<Enchantment> BUDDY_BOOST = ENCHANTMENTS.register("buddy_boost", EnchantmentBuddyBoost::new);
    public static final RegistryObject<Enchantment> RECOVERY = ENCHANTMENTS.register("recovery", EnchantmentRecovery::new);

    //Containers
    public static final RegistryObject<MenuType<BinderMenu>> BINDER_CONTAINER = MENUS.register("binder",
            () -> new MenuType<>((BinderMenu::new)));
    public static final RegistryObject<MenuType<DeckboxMenu>> DECKBOX_CONTAINER = MENUS.register("deckbox",
            () -> new MenuType<>((DeckboxMenu::new)));
    public static final RegistryObject<MenuType<PlaymatMenu>> PLAYMAT_CONTAINER = MENUS.register("playmat",
            () -> new MenuType<>((PlaymatMenu::new)));

    //GLMs
    public static RegistryObject<GlobalLootModifierSerializer<LootInjection.LootInjectionModifier>> LOOT_INJECTION = GLMS.register("loot_injection", LootInjection.LootInjectionSerializer::new);
}
