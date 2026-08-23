package com.wildcard.buddycards;

import com.wildcard.buddycards.registries.*;
import com.wildcard.buddycards.util.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Buddycards.MOD_ID)
public class Buddycards
{
    public static final String MOD_ID = "buddycards";

    public Buddycards(IEventBus eventBus, ModContainer modContainer) {
        eventBus.addListener(this::setup);

        BuddycardsComponents.registerComponents(eventBus);
        BuddycardsAttributes.registerAttributes(eventBus);
        BuddycardsBlocks.registerBlocks(eventBus);
        BuddycardsEntities.registerEntities(eventBus);
        BuddycardsItems.registerItems(eventBus);
        BuddycardsPotions.registerPotions(eventBus);
        BuddycardsMisc.registerStuff(eventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigManager.SPEC);
    }

    @SubscribeEvent
    private void setup(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new MobDropHandler());
        NeoForge.EVENT_BUS.register(new SpawnHandler());
        NeoForge.EVENT_BUS.register(new ExplosionHandler());
        NeoForge.EVENT_BUS.register(new DamageEffectHandler());
    }

    public static ResourceLocation buddycardsLocation(String string) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, string);
    }
}
