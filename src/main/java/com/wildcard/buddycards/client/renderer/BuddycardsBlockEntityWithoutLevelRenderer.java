package com.wildcard.buddycards.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wildcard.buddycards.client.BuddycardRenderType;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BuddycardsBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    public static final BuddycardsBlockEntityWithoutLevelRenderer instance = new BuddycardsBlockEntityWithoutLevelRenderer();

    public BuddycardsBlockEntityWithoutLevelRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        int glint = BuddycardItem.getFoil(pStack);
        pPoseStack.pushPose();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel bakedmodel = itemRenderer.getModel(pStack, Minecraft.getInstance().level, null, 1);
        bakedmodel.applyTransform(pDisplayContext, pPoseStack, false);
        for (BakedModel model : bakedmodel.getRenderPasses(pStack, true)) {
            VertexConsumer vertexconsumer = pBuffer.getBuffer(BuddycardRenderType.getRenderTypeForItem(glint));
            itemRenderer.renderModelLists(model, pStack, pPackedLight, pPackedOverlay, pPoseStack, vertexconsumer);
        }
        pPoseStack.popPose();
    }
}