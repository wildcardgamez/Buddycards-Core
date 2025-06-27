package com.wildcard.buddycards.datagen;

import com.wildcard.buddycards.Buddycards;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Buddycards.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Datagen {
    @SubscribeEvent
    static void onGatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(true, new ModelGen(event.getGenerator().getPackOutput(), Buddycards.MOD_ID, event.getExistingFileHelper()));
    }
}
