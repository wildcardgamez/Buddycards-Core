package com.wildcard.buddycards.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.client.BuddycardsLayers;
import com.wildcard.buddycards.client.model.MedalModel;
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
    public MedalRenderer(ResourceLocation texture) {
        this.texture = texture;
    }

    private final ResourceLocation texture;

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        MedalModel<LivingEntity> model = BuddycardsLayers.medal;
        model.setupAnim(slotContext.entity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.prepareMobModel(slotContext.entity(), limbSwing, limbSwingAmount, partialTicks);
        ICurioRenderer.followBodyRotations(slotContext.entity(), model);
        model.renderToBuffer(matrixStack, ItemRenderer.getFoilBuffer(renderTypeBuffer, BuddycardsLayers.medal.renderType(texture), false, stack.hasFoil()), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
