package com.wildcard.buddycards.worldgen;

import com.mojang.serialization.Codec;
import com.wildcard.buddycards.registries.BuddycardsBlocks;
import com.wildcard.buddycards.util.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LuminisVeinFeature extends Feature<NoneFeatureConfiguration> {
    public LuminisVeinFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext context) {
        if(!ConfigManager.luminisVeins.get() || ((context.origin().getX() / 16) % ConfigManager.luminisChunks.get() != 0) && ((context.origin().getZ() / 16) % ConfigManager.luminisChunks.get() != 0))
            return false;
        RandomSource rand = context.random();
        BlockPos pos = context.origin().offset(rand.nextInt(16), rand.nextInt(5, 64), rand.nextInt(16));
        WorldGenLevel lvl = context.level();
        if(pos.getY() < 0 && pos.getY() > -60 && context.level().getBlockState(pos).getBlock().equals(Blocks.LAVA)) {
            //2-4 branches
            for(int i = rand.nextInt(ConfigManager.luminisBranchMin.get(),ConfigManager.luminisBranchMax.get()); i > 0; i--) {
                //8-24 jumps
                BlockPos pos1, pos2 = pos.offset(rand.nextInt(-4, 4), rand.nextInt(-1, 3), rand.nextInt(-4, 4));
                for (int j = rand.nextInt(ConfigManager.luminisBranchLengthMin.get(),ConfigManager.luminisBranchLengthMax.get()); j > 0; j--) {
                    pos1 = pos2;
                    pos2 = pos2.offset(rand.nextInt(-3, 3), rand.nextInt(-2, 2), rand.nextInt(-3, 3));
                    if (pos2.getY() < -64)
                        continue;
                    for(BlockPos placePos : BlockPos.betweenClosed(pos1, pos2)) {
                        if(isStone(lvl.getBlockState(placePos)) && rand.nextFloat() < .5)
                            lvl.setBlock(placePos, placePos.getY() < 0 ? BuddycardsBlocks.DEEPSLATE_LUMINIS_ORE.get().defaultBlockState() : BuddycardsBlocks.LUMINIS_ORE.get().defaultBlockState(), 4);
                    }
                }
            }
            return true;
        }
        return false;
    }
}