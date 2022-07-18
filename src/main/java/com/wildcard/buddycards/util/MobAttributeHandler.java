package com.wildcard.buddycards.util;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.entity.EnderlingEntity;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Buddycards.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobAttributeHandler {
    @SubscribeEvent
    public static void setupAttributes(EntityAttributeCreationEvent event) {
        event.put(BuddycardsEntities.ENDERLING.get(), EnderlingEntity.setupAttributes());
    }
}
