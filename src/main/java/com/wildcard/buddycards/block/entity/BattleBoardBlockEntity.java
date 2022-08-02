package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BattleBoardBlockEntity extends BlockEntity {
    public BattleBoardBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public BattleBoardBlockEntity(BlockPos pos, BlockState state) {
        super(BuddycardsEntities.BATTLE_BOARD_ENTITY.get(), pos, state);
    }
}
