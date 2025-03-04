package com.wildcard.buddycards.block;

import com.wildcard.buddycards.block.entity.BuddysteelChargerBlockEntity;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BuddysteelChargerBlock extends BaseEntityBlock {
    public BuddysteelChargerBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BuddysteelChargerBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide && level.getBlockEntity(pos) instanceof BuddysteelChargerBlockEntity entity) {
            NetworkHooks.openScreen((ServerPlayer) player, entity, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BuddycardsEntities.BUDDYSTEEL_CHARGER_TILE.get(), BuddysteelChargerBlockEntity::tick);
    }
}
