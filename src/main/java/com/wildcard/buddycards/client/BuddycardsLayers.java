package com.wildcard.buddycards.client;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.client.model.BuddycardsArmorModel;
import com.wildcard.buddycards.client.model.MedalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Buddycards.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BuddycardsLayers {

    public static final ModelLayerLocation HEAD_LAYER = new ModelLayerLocation(new ResourceLocation(Buddycards.MOD_ID, "armor_head"), "main");
    public static final ModelLayerLocation CHEST_LAYER = new ModelLayerLocation(new ResourceLocation(Buddycards.MOD_ID, "armor_chest"), "main");
    public static final ModelLayerLocation LEGS_LAYER = new ModelLayerLocation(new ResourceLocation(Buddycards.MOD_ID, "armor_legs"), "main");
    public static final ModelLayerLocation FEET_LAYER = new ModelLayerLocation(new ResourceLocation(Buddycards.MOD_ID, "armor_feet"), "main");
    public static final ModelLayerLocation MEDAL_LAYER = new ModelLayerLocation(new ResourceLocation(Buddycards.MOD_ID, "medal"), "main");

    public static BuddycardsArmorModel<LivingEntity> helmet;
    public static BuddycardsArmorModel<LivingEntity> chestplate;
    public static BuddycardsArmorModel<LivingEntity> leggings;
    public static BuddycardsArmorModel<LivingEntity> boots;
    public static MedalModel<LivingEntity> medal;

    @SubscribeEvent
    public static void initLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(HEAD_LAYER, () -> BuddycardsArmorModel.createBodyLayer(EquipmentSlot.HEAD));
        event.registerLayerDefinition(CHEST_LAYER, () -> BuddycardsArmorModel.createBodyLayer(EquipmentSlot.CHEST));
        event.registerLayerDefinition(LEGS_LAYER, () -> BuddycardsArmorModel.createBodyLayer(EquipmentSlot.LEGS));
        event.registerLayerDefinition(FEET_LAYER, () -> BuddycardsArmorModel.createBodyLayer(EquipmentSlot.FEET));
        event.registerLayerDefinition(MEDAL_LAYER, MedalModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void initModels(EntityRenderersEvent.AddLayers event)
    {
        helmet = new BuddycardsArmorModel<>(event.getEntityModels().bakeLayer(HEAD_LAYER));
        chestplate = new BuddycardsArmorModel<>(event.getEntityModels().bakeLayer(CHEST_LAYER));
        leggings = new BuddycardsArmorModel<>(event.getEntityModels().bakeLayer(LEGS_LAYER));
        boots = new BuddycardsArmorModel<>(event.getEntityModels().bakeLayer(FEET_LAYER));
        medal = new MedalModel<>(event.getEntityModels().bakeLayer(MEDAL_LAYER));
    }

    public static BuddycardsArmorModel<LivingEntity> getArmor(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> helmet;
            case CHEST -> chestplate;
            case LEGS -> leggings;
            case FEET -> boots;
            default -> null;
        };
    }
}
