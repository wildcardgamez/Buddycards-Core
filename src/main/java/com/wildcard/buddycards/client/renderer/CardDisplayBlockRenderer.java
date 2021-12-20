package com.wildcard.buddycards.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.wildcard.buddycards.block.CardDisplayBlock;
import com.wildcard.buddycards.block.entity.CardDisplayBlockEntity;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class CardDisplayBlockRenderer<T extends CardDisplayBlockRenderer> implements BlockEntityRenderer<CardDisplayBlockEntity> {
    final static double[][] NORTH_POSITIONS= {{0.1875, 0.75, 0.125}, {0.5, 0.75, 0.13}, {0.8125, 0.75, 0.125}, {0.1875, 0.25, 0.125}, {0.5, 0.25, 0.13}, {0.8125, 0.25, 0.125}};
    final static double[][] EAST_POSITIONS= {{0.875, 0.75, 0.1875}, {0.87, 0.75, 0.5}, {0.875, 0.75, 0.8125}, {0.875, 0.25, 0.1875}, {0.87, 0.25, 0.5}, {0.875, 0.25, 0.8125}};
    final static double[][] SOUTH_POSITIONS= {{0.8125, 0.75, 0.875}, {0.5, 0.75, 0.87}, {0.1875, 0.75, 0.875}, {0.8125, 0.25, 0.875}, {0.5, 0.25, 0.87}, {0.1875, 0.25, 0.875}};
    final static double[][] WEST_POSITIONS= {{0.125, 0.75, 0.8125}, {0.13, 0.75, 0.5}, {0.125, 0.75, 0.1875}, {0.125, 0.25, 0.8125}, {0.13, 0.25, 0.5}, {0.125, 0.25, 0.1875}};

    public CardDisplayBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CardDisplayBlockEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        switch(tileEntityIn.getBlockState().getValue(CardDisplayBlock.DIR)) {
            case NORTH: {
                for(int i = 0; i < 6; i++) {
                    ItemStack itemstack = tileEntityIn.getCardInSlot(i+1);
                    if(itemstack.getItem() instanceof BuddycardItem) {
                        matrixStackIn.pushPose();
                        matrixStackIn.translate(NORTH_POSITIONS[i][0], NORTH_POSITIONS[i][1], NORTH_POSITIONS[i][2]);
                        matrixStackIn.scale(0.5f, 0.5f, 0.5f);
                        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
                        BakedModel ibakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemstack, tileEntityIn.getLevel(), null, 0);
                        Minecraft.getInstance().getItemRenderer().render(itemstack, ItemTransforms.TransformType.FIXED, true, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, ibakedmodel);
                        matrixStackIn.popPose();
                    }
                }
                break;
            }
            case EAST: {
                for(int i = 0; i < 6; i++) {
                    ItemStack itemstack = tileEntityIn.getCardInSlot(i+1);
                    if(itemstack.getItem() instanceof BuddycardItem) {
                        matrixStackIn.pushPose();
                        matrixStackIn.translate(EAST_POSITIONS[i][0], EAST_POSITIONS[i][1], EAST_POSITIONS[i][2]);
                        matrixStackIn.scale(0.5f, 0.5f, 0.5f);
                        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
                        BakedModel ibakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemstack, tileEntityIn.getLevel(), null, 0);
                        Minecraft.getInstance().getItemRenderer().render(itemstack, ItemTransforms.TransformType.FIXED, true, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, ibakedmodel);
                        matrixStackIn.popPose();
                    }
                }
                break;
            }
            case SOUTH: {
                for(int i = 0; i < 6; i++) {
                    ItemStack itemstack = tileEntityIn.getCardInSlot(i+1);
                    if(itemstack.getItem() instanceof BuddycardItem) {
                        matrixStackIn.pushPose();
                        matrixStackIn.translate(SOUTH_POSITIONS[i][0], SOUTH_POSITIONS[i][1], SOUTH_POSITIONS[i][2]);
                        matrixStackIn.scale(0.5f, 0.5f, 0.5f);
                        BakedModel ibakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemstack, tileEntityIn.getLevel(), null, 0);
                        Minecraft.getInstance().getItemRenderer().render(itemstack, ItemTransforms.TransformType.FIXED, true, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, ibakedmodel);
                        matrixStackIn.popPose();
                    }
                }
                break;
            }
            case WEST: {
                for(int i = 0; i < 6; i++) {
                    ItemStack itemstack = tileEntityIn.getCardInSlot(i+1);
                    if(itemstack.getItem() instanceof BuddycardItem) {
                        matrixStackIn.pushPose();
                        matrixStackIn.translate(WEST_POSITIONS[i][0], WEST_POSITIONS[i][1], WEST_POSITIONS[i][2]);
                        matrixStackIn.scale(0.5f, 0.5f, 0.5f);
                        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(270));
                        BakedModel ibakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemstack, tileEntityIn.getLevel(), null, 0);
                        Minecraft.getInstance().getItemRenderer().render(itemstack, ItemTransforms.TransformType.FIXED, true, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, ibakedmodel);
                        matrixStackIn.popPose();
                    }
                }
            }
        }
    }
}

