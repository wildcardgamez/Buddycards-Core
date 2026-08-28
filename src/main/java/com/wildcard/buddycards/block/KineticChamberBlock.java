package com.wildcard.buddycards.block;

import com.mojang.serialization.MapCodec;
import com.wildcard.buddycards.block.entity.GraderBlockEntity;
import com.wildcard.buddycards.block.entity.KineticChamberBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class KineticChamberBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(1, 1, 1, 15, 15, 15),
            Block.box(0, 0, 0, 6, 6, 6),
            Block.box(0, 10, 0, 6, 16, 6),
            Block.box(0, 0, 10, 6, 6, 16),
            Block.box(0, 10, 10, 6, 16, 16),
            Block.box(10, 0, 0, 16, 6, 6),
            Block.box(10, 10, 0, 16, 16, 6),
            Block.box(10, 0, 10, 16, 6, 16),
            Block.box(10, 10, 10, 16, 16, 16)
    );
    public static final MapCodec<KineticChamberBlock> CODEC = simpleCodec(KineticChamberBlock::new);

    public KineticChamberBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KineticChamberBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof KineticChamberBlockEntity entity && level instanceof ServerLevel) {
            if(!entity.getItemSlot().isEmpty()) {
                ItemStack oldItem = entity.getItemSlot();
                entity.setItemSlot(ItemStack.EMPTY);
                if(!player.addItem(oldItem))
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), oldItem);
            }
        }
        level.updateNeighbourForOutputSignal(pos, this);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof KineticChamberBlockEntity entity && level instanceof ServerLevel) {
            if(!entity.getItemSlot().isEmpty()) {
                ItemStack oldItem = entity.getItemSlot();
                entity.setItemSlot(stack.split(1));
                if(!player.addItem(oldItem))
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), oldItem);
            }
            else if(!stack.isEmpty())
                entity.setItemSlot(stack.split(1));
        }
        level.updateNeighbourForOutputSignal(pos, this);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.getBlock().equals(newState.getBlock()) && level.getBlockEntity(pos) instanceof KineticChamberBlockEntity entity && !entity.getItemSlot().isEmpty())
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), entity.getItemSlot());
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof KineticChamberBlockEntity entity && !entity.getItemSlot().isEmpty())
            return 10;
        else
            return 0;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }
}
