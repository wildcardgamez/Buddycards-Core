package com.wildcard.buddycards.util;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.client.model.EnderlingModel;
import com.wildcard.buddycards.client.renderer.CardDisplayBlockRenderer;
import com.wildcard.buddycards.client.renderer.EnderlingRenderer;
import com.wildcard.buddycards.client.renderer.PlaymatBlockRenderer;
import com.wildcard.buddycards.integration.CuriosIntegration;
import com.wildcard.buddycards.screens.ChargerScreen;
import com.wildcard.buddycards.screens.DeckboxScreen;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import com.wildcard.buddycards.screens.PlaymatScreen;
import com.wildcard.buddycards.screens.BinderScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Buddycards.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientStuff {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(BuddycardsMisc.BINDER_MENU.get(), BinderScreen::new));
        event.enqueueWork(() -> MenuScreens.register(BuddycardsMisc.DECKBOX_CONTAINER.get(), DeckboxScreen::new));
        event.enqueueWork(() -> MenuScreens.register(BuddycardsMisc.PLAYMAT_CONTAINER.get(), PlaymatScreen::new));
        event.enqueueWork(() -> MenuScreens.register(BuddycardsMisc.CHARGER_CONTAINER.get(), ChargerScreen::new));
        for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
            for (BuddycardItem card : set.getCards()) {
                event.enqueueWork(() -> ItemProperties.register(card, new ResourceLocation(Buddycards.MOD_ID, "grade"), (stack, world, entity, idk) -> {
                    if (stack.getTag() != null)
                        return stack.getTag().getInt("grade");
                    return 0;
                }));
            }
        }
        event.enqueueWork(() -> ItemProperties.register(BuddycardsItems.BUDDYSTEEL_POWER_METER.get(), new ResourceLocation(Buddycards.MOD_ID, "power"), (stack, world, entity, idk) -> {
            if (stack.getTag() != null)
                return stack.getTag().getInt("power");
            return 0;
        }));
        event.enqueueWork(() -> ItemProperties.register(BuddycardsItems.LUMINIS_POWER_METER.get(), new ResourceLocation(Buddycards.MOD_ID, "power"), (stack, world, entity, idk) -> {
            if (stack.getTag() != null)
                return stack.getTag().getInt("power");
            return 0;
        }));
        event.enqueueWork(() -> ItemProperties.register(BuddycardsItems.ZYLEX_POWER_METER.get(), new ResourceLocation(Buddycards.MOD_ID, "power"), (stack, world, entity, idk) -> {
            if (stack.getTag() != null)
                return stack.getTag().getInt("power");
            return 0;
        }));
        event.enqueueWork(() -> ItemProperties.register(BuddycardsItems.CHARGED_BUDDYSTEEL_POWER_METER.get(), new ResourceLocation(Buddycards.MOD_ID, "power"), (stack, world, entity, idk) -> {
            if (stack.getTag() != null)
                return stack.getTag().getInt("power");
            return 0;
        }));
        event.enqueueWork(() -> ItemProperties.register(BuddycardsItems.BUDDYSTEEL_DECKBOX.get(), new ResourceLocation(Buddycards.MOD_ID, "full"), (stack, world, entity, idk) -> {
            if (stack.getTag() != null)
                return stack.getTag().getInt("full");
            return 0;
        }));
        event.enqueueWork(() -> ItemProperties.register(BuddycardsItems.LUMINIS_DECKBOX.get(), new ResourceLocation(Buddycards.MOD_ID,"full"), (stack, world, entity, idk) -> {
            if (stack.getTag() != null)
                return stack.getTag().getInt("full");
            return 0;
        }));
        event.enqueueWork(() -> ItemProperties.register(BuddycardsItems.ZYLEX_DECKBOX.get(), new ResourceLocation(Buddycards.MOD_ID,"full"), (stack, world, entity, idk) -> {
            if (stack.getTag() != null)
                return stack.getTag().getInt("full");
            return 0;
        }));
        CuriosIntegration.setupRenderers();
    }

    public static ModelLayerLocation ENDERLING_LAYER = new ModelLayerLocation(new ResourceLocation(Buddycards.MOD_ID, "enderling"), "enderling");

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BuddycardsEntities.ENDERLING.get(), EnderlingRenderer::new);
        event.registerBlockEntityRenderer(BuddycardsEntities.CARD_DISPLAY_ENTITY.get(), CardDisplayBlockRenderer::new);
        event.registerBlockEntityRenderer(BuddycardsEntities.PLAYMAT_ENTITY.get(), PlaymatBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ENDERLING_LAYER, EnderlingModel::createBodyLayer);
    }
}
