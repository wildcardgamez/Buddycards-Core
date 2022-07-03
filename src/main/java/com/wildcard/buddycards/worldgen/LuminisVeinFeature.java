package com.wildcard.buddycards.worldgen;

import com.mojang.serialization.Codec;
import com.wildcard.buddycards.registries.BuddycardsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class LuminisVeinFeature extends Feature<NoneFeatureConfiguration> {
    public LuminisVeinFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext context) {
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        Random random = context.random();

        for(; blockpos.getY() > worldgenlevel.getMinBuildHeight() + 3; blockpos = blockpos.below()) {
            if (!worldgenlevel.isEmptyBlock(blockpos.below())) {
                BlockState blockstate = worldgenlevel.getBlockState(blockpos.below());
                if (isDirt(blockstate) || isStone(blockstate)) {
                    break;
                }
            }
        }

        if (blockpos.getY() <= worldgenlevel.getMinBuildHeight() + 10) {
            return false;
        } else {
            for(int l = 0; l < 7; ++l) {
                int i = random.nextInt(8);
                int j = random.nextInt(8);
                int k = random.nextInt(8);
                float f = (float)(i + j + k) * 0.333F + 0.5F;

                for(BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-i, -j, -k), blockpos.offset(i, j, k))) {
                    if (blockpos1.distSqr(blockpos) <= (double)(f * f)) {
                        if(blockpos1.getY() > 0)
                            worldgenlevel.setBlock(blockpos1, BuddycardsBlocks.LUMINIS_ORE.get().defaultBlockState(), 4);
                        else
                            worldgenlevel.setBlock(blockpos1, BuddycardsBlocks.DEEPSLATE_LUMINIS_ORE.get().defaultBlockState(), 4);
                    }
                }

                blockpos = blockpos.offset(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
            }
            return true;
        }
    }
}