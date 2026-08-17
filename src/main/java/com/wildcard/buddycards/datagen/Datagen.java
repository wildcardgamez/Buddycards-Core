package com.wildcard.buddycards.datagen;

import com.wildcard.buddycards.Buddycards;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(value = Buddycards.MOD_ID)
@EventBusSubscriber(modid = Buddycards.MOD_ID)
public class Datagen {
    @SubscribeEvent
    static void onGatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(true, new ModelGen(event.getGenerator().getPackOutput(), Buddycards.MOD_ID, event.getExistingFileHelper()));
    }
}