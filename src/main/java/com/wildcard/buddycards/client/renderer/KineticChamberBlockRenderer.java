package com.wildcard.buddycards.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wildcard.buddycards.block.entity.KineticChamberBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class KineticChamberBlockRenderer implements BlockEntityRenderer<KineticChamberBlockEntity> {
    public KineticChamberBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(KineticChamberBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
            ItemStack itemstack = blockEntity.getItemSlot();
            if (!itemstack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(.5, .5, .5);
                poseStack.scale(0.5f, 0.5f, 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getLevel().getGameTime() % 360));
                BakedModel ibakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemstack, blockEntity.getLevel(), null, 0);
                Minecraft.getInstance().getItemRenderer().render(itemstack, ItemDisplayContext.FIXED, true, poseStack, bufferIn, combinedLightIn, combinedOverlayIn, ibakedmodel);
                poseStack.popPose();
            }
    }
}

