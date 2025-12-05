package com.wildcard.buddycards.client.renderer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wildcard.buddycards.block.CardDisplayBlock;
import com.wildcard.buddycards.block.entity.CardDisplayBlockEntity;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardDisplayBlockRenderer<T extends CardDisplayBlockRenderer> implements BlockEntityRenderer<CardDisplayBlockEntity> {
    private static final double[][] NORTH_POSITIONS= {{0.1875, 0.75, 0.125}, {0.5, 0.75, 0.13}, {0.8125, 0.75, 0.125}, {0.1875, 0.25, 0.125}, {0.5, 0.25, 0.13}, {0.8125, 0.25, 0.125}};
    private static final double[][] EAST_POSITIONS= {{0.875, 0.75, 0.1875}, {0.87, 0.75, 0.5}, {0.875, 0.75, 0.8125}, {0.875, 0.25, 0.1875}, {0.87, 0.25, 0.5}, {0.875, 0.25, 0.8125}};
    private static final double[][] SOUTH_POSITIONS= {{0.8125, 0.75, 0.875}, {0.5, 0.75, 0.87}, {0.1875, 0.75, 0.875}, {0.8125, 0.25, 0.875}, {0.5, 0.25, 0.87}, {0.1875, 0.25, 0.875}};
    private static final double[][] WEST_POSITIONS= {{0.125, 0.75, 0.8125}, {0.13, 0.75, 0.5}, {0.125, 0.75, 0.1875}, {0.125, 0.25, 0.8125}, {0.13, 0.25, 0.5}, {0.125, 0.25, 0.1875}};

    private static final Map<Direction, List<Vec3>> positions = Util.make(() -> {
        Map<Direction, List<Vec3>> tempMap = new HashMap<>();
        tempMap.put(Direction.NORTH, fromDoubleArr(NORTH_POSITIONS));
        tempMap.put(Direction.EAST, fromDoubleArr(EAST_POSITIONS));
        tempMap.put(Direction.SOUTH, fromDoubleArr(SOUTH_POSITIONS));
        tempMap.put(Direction.WEST, fromDoubleArr(WEST_POSITIONS));
        return ImmutableMap.copyOf(tempMap);
    });

    private static List<Vec3> fromDoubleArr(double[][] arr) {
        List<Vec3> tempList = new ArrayList<>();
        for (double[] doubles : arr) {
            tempList.add(new Vec3(doubles[0], doubles[1], doubles[2]));
        }
        return ImmutableList.copyOf(tempList);
    }

    public CardDisplayBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CardDisplayBlockEntity tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = tileEntityIn.getBlockState().getValue(CardDisplayBlock.DIR);
        List<Vec3> vec3s = positions.get(direction);
        for (int i = 0; i < 6; i++) {
            ItemStack itemstack = tileEntityIn.getCardInSlot(i+1);
            if (itemstack.getItem() instanceof BuddycardItem) {
                poseStack.pushPose();
                Vec3 vec3 = vec3s.get(i);
                poseStack.translate(vec3.x(), vec3.y(), vec3.z());
                poseStack.scale(0.5f, 0.5f, 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(360 - direction.get2DDataValue()*90));
                BakedModel ibakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemstack, tileEntityIn.getLevel(), null, 0);
                Minecraft.getInstance().getItemRenderer().render(itemstack, ItemDisplayContext.FIXED, true, poseStack, bufferIn, combinedLightIn, combinedOverlayIn, ibakedmodel);
                poseStack.popPose();
            }
        }
    }
}

