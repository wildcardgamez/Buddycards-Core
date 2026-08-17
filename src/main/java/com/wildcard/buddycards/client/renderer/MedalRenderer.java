package com.wildcard.buddycards.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.client.BuddycardsLayers;
import com.wildcard.buddycards.client.model.MedalModel;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class MedalRenderer implements ICurioRenderer {
    public MedalRenderer(String id) {
        this.id = id;
    }

    private final String id;

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        MedalModel<LivingEntity> model = BuddycardsLayers.medal;
        model.setupAnim(slotContext.entity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.prepareMobModel(slotContext.entity(), limbSwing, limbSwingAmount, partialTicks);
        ICurioRenderer.followBodyRotations(slotContext.entity(), model);
        int i = stack.has(BuddycardsComponents.COLLECTION_TIER) ? stack.get(BuddycardsComponents.COLLECTION_TIER) : -1;
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, i == -1 ? id + ".png" : id + i + ".png");
        model.renderToBuffer(matrixStack, ItemRenderer.getFoilBuffer(renderTypeBuffer, BuddycardsLayers.medal.renderType(texture), false, stack.hasFoil()), light, OverlayTexture.NO_OVERLAY);
    }
}
