package com.wildcard.buddycards;

import com.wildcard.buddycards.battles.BuddycardsPackets;
import com.wildcard.buddycards.integration.CuriosIntegration;
import com.wildcard.buddycards.registries.BuddycardsBlocks;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import com.wildcard.buddycards.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Buddycards.MOD_ID)
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

        MinecraftForge.EVENT_BUS.register(this);
        BuddycardsPackets.registerPackets();
        FMLJavaModLoadingContext.get().getModEventBus().register(CuriosIntegration.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new MobDropHandler());
        MinecraftForge.EVENT_BUS.register(new SpawnHandler());
        MinecraftForge.EVENT_BUS.register(new ExplosionHandler());
        MinecraftForge.EVENT_BUS.register(new DamageEffectHandler());
    }
}
