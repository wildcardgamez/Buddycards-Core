package com.wildcard.buddycards.client.renderer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wildcard.buddycards.block.CardStandBlock;
import com.wildcard.buddycards.block.entity.CardStandBlockEntity;
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

public class CardStandBlockRenderer implements BlockEntityRenderer<CardStandBlockEntity> {
    private static final double[][] NORTH_POSITIONS= {
            {0.1875, 0.6875, 0.125}, {0.5, 0.6875, 0.12}, {0.8125, 0.6875, 0.125},
            {0.1875, 0.5625, 0.375}, {0.5, 0.5625, 0.37}, {0.8125, 0.5625, 0.375},
            {0.1875, 0.4375, 0.625}, {0.5, 0.4375, 0.62}, {0.8125, 0.3125, 0.625},
            {0.1875, 0.4375, 0.875}, {0.5, 0.3125, 0.87}, {0.8125, 0.3125, 0.875}
    };
    private static final double[][] EAST_POSITIONS = {
            {0.875, 0.6875, 0.1875}, {0.88, 0.6875, 0.5}, {0.875, 0.6875, 0.8125},
            {0.625, 0.5625, 0.1875}, {0.63, 0.5625, 0.5}, {0.625, 0.5625, 0.8125},
            {0.375, 0.4375, 0.1875}, {0.38, 0.4375, 0.5}, {0.375, 0.3125, 0.8125},
            {0.125, 0.4375, 0.1875}, {0.13, 0.3125, 0.5}, {0.125, 0.3125, 0.8125}
    };
    private static final double[][] SOUTH_POSITIONS = {
            {0.8125, 0.6875, 0.875}, {0.5, 0.6875, 0.88}, {0.1875, 0.6875, 0.875},
            {0.8125, 0.5625, 0.625}, {0.5, 0.5625, 0.63}, {0.1875, 0.5625, 0.625},
            {0.8125, 0.4375, 0.375}, {0.5, 0.4375, 0.38}, {0.1875, 0.3125, 0.375},
            {0.8125, 0.4375, 0.125}, {0.5, 0.3125, 0.13}, {0.1875, 0.3125, 0.125}
    };
    private static final double[][] WEST_POSITIONS = {
            {0.125, 0.6875, 0.8125}, {0.12, 0.6875, 0.5}, {0.125, 0.6875, 0.1875},
            {0.375, 0.5625, 0.8125}, {0.37, 0.5625, 0.5}, {0.375, 0.5625, 0.1875},
            {0.625, 0.4375, 0.8125}, {0.62, 0.4375, 0.5}, {0.625, 0.3125, 0.1875},
            {0.875, 0.4375, 0.8125}, {0.87, 0.3125, 0.5}, {0.875, 0.3125, 0.1875}
    };

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

    public CardStandBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CardStandBlockEntity tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        System.out.println("uh");
        Direction direction = tileEntityIn.getBlockState().getValue(CardStandBlock.DIR);
        List<Vec3> vec3s = positions.get(direction);
        for (int i = 0; i < 12; i++) {
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

