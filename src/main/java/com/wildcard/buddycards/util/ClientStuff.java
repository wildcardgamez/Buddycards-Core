package com.wildcard.buddycards.util;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.client.renderer.CardDisplayBlockRenderer;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import com.wildcard.buddycards.screen.BinderScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.HashMap;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Buddycards.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientStuff {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(BuddycardsMisc.BINDER_CONTAINER.get(), BinderScreen::new));
        for (ArrayList<BuddycardItem> set : BuddycardItem.CARD_LIST.values()) {
            for (BuddycardItem card : set) {
                event.enqueueWork(() -> ItemProperties.register(card, new ResourceLocation("grade"), (stack, world, entity, idk) -> {
                    if (stack.getTag() != null)
                        return stack.getTag().getInt("grade");
                    return 0;
                }));
            }
        }
        event.enqueueWork(() -> ItemProperties.register(BuddycardsItems.BUDDYSTEEL_POWER_METER.get(), new ResourceLocation("power"), (stack, world, entity, idk) -> {
            if (stack.getTag() != null)
                return stack.getTag().getInt("power");
            return 0;
        }));
        CuriosIntegration.setupRenderers();
    }

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BuddycardsEntities.CARD_DISPLAY_TILE.get(), CardDisplayBlockRenderer::new);
    }
}
