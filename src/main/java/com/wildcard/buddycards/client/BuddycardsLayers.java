package com.wildcard.buddycards.client;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.client.model.MedalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Buddycards.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BuddycardsLayers {

    public static final ModelLayerLocation MEDAL_LAYER = new ModelLayerLocation(new ResourceLocation(Buddycards.MOD_ID, "medal"), "main");

    public static MedalModel<LivingEntity> medal;

    @SubscribeEvent
    public static void initLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(MEDAL_LAYER, MedalModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void initModels(EntityRenderersEvent.AddLayers event)
    {
        medal = new MedalModel<>(event.getEntityModels().bakeLayer(MEDAL_LAYER));
    }
}
