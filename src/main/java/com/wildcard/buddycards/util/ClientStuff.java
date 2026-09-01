package com.wildcard.buddycards.util;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.client.model.EnderlingModel;
import com.wildcard.buddycards.client.renderer.*;
import com.wildcard.buddycards.item.tiered.ICollectionTieredItem;
import com.wildcard.buddycards.integration.CuriosIntegration;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import com.wildcard.buddycards.screens.*;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

@Mod(value = Buddycards.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Buddycards.MOD_ID, value = Dist.CLIENT)
public class ClientStuff {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        for (BuddycardSet set : BuddycardsAPI.getAllSets()) {
            for (BuddycardItem card : set.getCards()) {
                event.enqueueWork(() -> ItemProperties.register(card, Buddycards.buddycardsLocation("foil"), (stack, world, entity, idk) -> {
                    if (stack.has(BuddycardsComponents.BUDDYCARD_FOIL))
                        return stack.get(BuddycardsComponents.BUDDYCARD_FOIL);
                    return 0;
                }));
                event.enqueueWork(() -> ItemProperties.register(card, Buddycards.buddycardsLocation("grade"), (stack, world, entity, idk) -> {
                    if (stack.has(BuddycardsComponents.BUDDYCARD_GRADE))
                        return stack.get(BuddycardsComponents.BUDDYCARD_GRADE);
                    return 0;
                }));
            }
        }
        event.enqueueWork(() -> ItemProperties.register(BuddycardsItems.BUDDYSTEEL_SCANNER.get(), Buddycards.buddycardsLocation("tier"), (stack, world, entity, idk) -> {
            if (stack.has(BuddycardsComponents.COLLECTION_TIER))
                return stack.get(BuddycardsComponents.COLLECTION_TIER);
            return 0;
        }));
        for (DeferredHolder<Item, ? extends Item> item : BuddycardsItems.ITEMS.getEntries()) {
            if (item.get() instanceof ICollectionTieredItem)
                event.enqueueWork(() -> ItemProperties.register(item.get(), Buddycards.buddycardsLocation("tier"), (stack, world, entity, idk) -> {
                    if (stack.has(BuddycardsComponents.COLLECTION_TIER))
                        return stack.get(BuddycardsComponents.COLLECTION_TIER);
                    return 0;
                }));
        }
        CuriosIntegration.setupRenderers();
    }

    public static ModelLayerLocation ENDERLING_LAYER = new ModelLayerLocation(Buddycards.buddycardsLocation("enderling"), "enderling");

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BuddycardsEntities.ENDERLING.get(), EnderlingRenderer::new);
        event.registerBlockEntityRenderer(BuddycardsEntities.CARD_DISPLAY_ENTITY.get(), CardDisplayBlockRenderer::new);
        event.registerBlockEntityRenderer(BuddycardsEntities.CARD_STAND_ENTITY.get(), CardStandBlockRenderer::new);
        event.registerBlockEntityRenderer(BuddycardsEntities.KINETIC_CHAMBER_ENTITY.get(), KineticChamberBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ENDERLING_LAYER, EnderlingModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(BuddycardsMisc.BINDER_MENU.get(), BinderScreen::new);
        event.register(BuddycardsMisc.SCANNER_MENU.get(), ScannerScreen::new);
        event.register(BuddycardsMisc.GRADER_MENU.get(), GraderScreen::new);
        event.register(BuddycardsMisc.CHARGER_MENU.get(), ChargerScreen::new);
    }
}
