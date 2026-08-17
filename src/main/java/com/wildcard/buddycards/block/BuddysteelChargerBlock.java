package com.wildcard.buddycards.block;

import com.mojang.serialization.MapCodec;
import com.wildcard.buddycards.block.entity.BuddysteelChargerBlockEntity;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BuddysteelChargerBlock extends BaseEntityBlock {
    public static final MapCodec<? extends BuddysteelChargerBlock> CODEC = simpleCodec(BuddysteelChargerBlock::new);

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 5, 16),
            Block.box(4, 5, 4, 12, 11, 12),
            Block.box(0, 11, 0, 16, 16, 16)
    );

    public BuddysteelChargerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BuddysteelChargerBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        if (!level.isClientSide)
            return createTickerHelper(entityType, BuddycardsEntities.CHARGER_ENTITY.get(), BuddysteelChargerBlockEntity::tick);
        else
            return null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.getBlock().equals(newState.getBlock()) && level.getBlockEntity(pos) instanceof BuddysteelChargerBlockEntity entity) {
            SimpleContainer inventory = new SimpleContainer(7);
            for (int i = 0; i < 7; i++)
                inventory.setItem(i, entity.getInventory().getStackInSlot(i));
            Containers.dropContents(level, pos, inventory);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof BuddysteelChargerBlockEntity entity) {
            serverPlayer.openMenu(entity, pos);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof BuddysteelChargerBlockEntity entity) {
            serverPlayer.openMenu(entity, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
