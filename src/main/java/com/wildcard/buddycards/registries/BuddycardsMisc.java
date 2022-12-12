package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
<<<<<<< Updated upstream
import com.wildcard.buddycards.screens.BattleBoardContainer;
import com.wildcard.buddycards.screens.BinderScreen;
import com.wildcard.buddycards.screens.DeckboxScreen;
=======
import com.wildcard.buddycards.menu.DeckboxMenu;
import com.wildcard.buddycards.menu.PlaymatMenu;
import com.wildcard.buddycards.menu.BinderMenu;
>>>>>>> Stashed changes
import com.wildcard.buddycards.enchantment.EnchantmentBuddyBoost;
import com.wildcard.buddycards.enchantment.EnchantmentExtraPage;
import com.wildcard.buddycards.enchantment.EnchantmentRecovery;
import com.wildcard.buddycards.util.LootInjection;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BuddycardsMisc {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Buddycards.MOD_ID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Buddycards.MOD_ID);
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLMS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, Buddycards.MOD_ID);

    public static void registerStuff() {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        GLMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Enchantment
    public static final RegistryObject<Enchantment> EXTRA_PAGE = ENCHANTMENTS.register("extra_page", EnchantmentExtraPage::new);
    public static final RegistryObject<Enchantment> BUDDY_BOOST = ENCHANTMENTS.register("buddy_boost", EnchantmentBuddyBoost::new);
    public static final RegistryObject<Enchantment> RECOVERY = ENCHANTMENTS.register("recovery", EnchantmentRecovery::new);

    //Containers
<<<<<<< Updated upstream
    public static final RegistryObject<MenuType<BinderScreen>> BINDER_CONTAINER = CONTAINERS.register("binder",
            () -> new MenuType<>((BinderScreen::new)));
    public static final RegistryObject<MenuType<DeckboxScreen>> DECKBOX_CONTAINER = CONTAINERS.register("deckbox",
            () -> new MenuType<>((DeckboxScreen::new)));
    public static final RegistryObject<MenuType<BattleBoardContainer>> BATTLE_BOARD_CONTAINER = CONTAINERS.register("battle_board",
            () -> IForgeMenuType.create((id, inv, data) -> new BattleBoardContainer(id, inv, data.readBlockPos())));
=======
    public static final RegistryObject<MenuType<BinderMenu>> BINDER_CONTAINER = CONTAINERS.register("binder",
            () -> new MenuType<>((BinderMenu::new)));
    public static final RegistryObject<MenuType<DeckboxMenu>> DECKBOX_CONTAINER = CONTAINERS.register("deckbox",
            () -> new MenuType<>((DeckboxMenu::new)));
    public static final RegistryObject<MenuType<PlaymatMenu>> PLAYMAT_CONTAINER = CONTAINERS.register("playmat",
            () -> IForgeMenuType.create((id, inv, data) -> new PlaymatMenu(id, inv, data.readBlockPos())));
>>>>>>> Stashed changes

    //GLMs
    public static RegistryObject<GlobalLootModifierSerializer<LootInjection.LootInjectionModifier>> LOOT_INJECTION = GLMS.register("loot_injection", LootInjection.LootInjectionSerializer::new);
}
