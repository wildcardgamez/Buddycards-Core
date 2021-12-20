package com.wildcard.buddycards.datagen;

import com.wildcard.buddycards.Buddycards;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Buddycards.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Datagen {
    @SubscribeEvent
    static void onGatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(new CardModelGen(event.getGenerator(), Buddycards.MOD_ID, event.getExistingFileHelper()));
    }
}
