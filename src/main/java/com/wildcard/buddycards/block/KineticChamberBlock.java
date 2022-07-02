package com.wildcard.buddycards.block;

import com.wildcard.buddycards.block.entity.KineticChamberBlockEntity;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class KineticChamberBlock extends BaseEntityBlock {
    public KineticChamberBlock(Properties properties) {
        super(properties);
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof KineticChamberBlockEntity entity) {
            ItemStack stack = player.getItemInHand(hand);
            if(!entity.getItemSlot().isEmpty()) {
                ItemStack oldItem = entity.getItemSlot();
                if (stack.getItem() instanceof BuddycardItem) {
                    entity.setItemSlot(new ItemStack(stack.getItem()));
                    stack.shrink(1);
                }
                else {
                    entity.setItemSlot(ItemStack.EMPTY);
                }
                if(!player.addItem(oldItem))
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), oldItem);
            }
            else if(!stack.isEmpty()) {
                entity.setItemSlot(new ItemStack(stack.getItem()));
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level.getBlockEntity(pos) instanceof KineticChamberBlockEntity entity) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), entity.getItemSlot());
            entity.setItemSlot(ItemStack.EMPTY);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof KineticChamberBlockEntity entity && !entity.getItemSlot().isEmpty()) {
            return 10;
        }
        else
            return 0;
    }
}
