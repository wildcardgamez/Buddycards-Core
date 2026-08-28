package com.wildcard.buddycards.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public interface CardInfoProviderBlock {
    Stream<CardInfo> getAllCardInfo(BlockState blockState, Level world, BlockPos pos, @Nullable Player player);
}
