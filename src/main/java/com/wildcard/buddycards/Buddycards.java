package com.wildcard.buddycards;

import com.wildcard.buddycards.registries.*;
import com.wildcard.buddycards.util.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("buddycards")
public class Buddycards
{
    public static final String MOD_ID = "buddycards";

    public Buddycards() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.config);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        ConfigManager.loadConfig(FMLPaths.CONFIGDIR.get().resolve("buddycards-common.toml").toString());

        CuriosIntegration.imc();

        BuddycardsBlocks.registerBlocks();
        BuddycardsEntities.registerEntities();
        BuddycardsItems.registerItems();
        BuddycardsMisc.registerStuff();
        BuddycardsFeatures.registerFeatures();

        MinecraftForge.EVENT_BUS.register(this);

        FMLJavaModLoadingContext.get().getModEventBus().register(CuriosIntegration.class);
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("buddycards") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BuddycardsItems.PACK_BASE.get());
        }
    };

    public static final CreativeModeTab CARDS_TAB = new CreativeModeTab("buddycards_cards") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BuddycardsItems.PACK_BASE.get().rollCard(new Random()));
        }
    };

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new MobDropHandler());
        MinecraftForge.EVENT_BUS.register(new ExplosionHandler());
        MinecraftForge.EVENT_BUS.register(new DamageEffectHandler());
        MinecraftForge.EVENT_BUS.register(new BiomeLoadingHandler());
    }
}
