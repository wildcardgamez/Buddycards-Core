package com.wildcard.buddycards.block;

import com.google.common.collect.ImmutableMap;
import com.wildcard.buddycards.block.entity.CardStandBlockEntity;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CardStandBlock extends BaseEntityBlock {
    public static final DirectionProperty DIR = BlockStateProperties.HORIZONTAL_FACING;
    private static final Map<Direction, VoxelShape> SHAPES = Util.make(() -> {
        Map<Direction, VoxelShape> shape = new HashMap<>();
        shape.put(Direction.NORTH, Shapes.or(Block.box(0, 0, 12, 16, 2, 16), Block.box(0, 0, 8, 16, 4, 12), Block.box(0, 0, 4, 16, 6, 8), Block.box(0, 0, 0, 16, 8, 4)));
        shape.put(Direction.EAST, Shapes.or(Block.box(0, 0, 0, 4, 2, 16), Block.box(4, 0, 0, 8, 4, 16), Block.box(8, 0, 0, 12, 6, 16), Block.box(12, 0, 0, 16, 8, 16)));
        shape.put(Direction.SOUTH, Shapes.or(Block.box(0, 0, 0, 16, 2, 4), Block.box(0, 0, 4, 16, 4, 8), Block.box(0, 0, 8, 16, 6, 12), Block.box(0, 0, 12, 16, 8, 16)));
        shape.put(Direction.WEST, Shapes.or(Block.box(12, 0, 0, 16, 2, 16), Block.box(8, 0, 0, 12, 4, 16), Block.box(4, 0, 0, 8, 6, 16), Block.box(0, 0, 0, 4, 8, 16)));
        return ImmutableMap.copyOf(shape);
    });

    public CardStandBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof CardStandBlockEntity standEntity && level instanceof ServerLevel) {
            int slot = getSlot(state.getValue(DIR), hit.getLocation());
            ItemStack stack = player.getItemInHand(hand);
            if(standEntity.getCardInSlot(slot).getItem() instanceof BuddycardItem) {
                ItemStack oldCard = standEntity.getCardInSlot(slot);
                if (stack.getItem() instanceof BuddycardItem)
                    standEntity.putCardInSlot(stack.split(1), slot);
                else
                    standEntity.putCardInSlot(ItemStack.EMPTY, slot);
                if(!player.addItem(oldCard))
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), oldCard);
            }
            else if(stack.getItem() instanceof BuddycardItem) {
                standEntity.putCardInSlot(stack.split(1), slot);
            }
        }
        level.updateNeighbourForOutputSignal(pos, this);
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CardStandBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(DIR));
    }

    @Override
    public BlockState getStateForPlacement (BlockPlaceContext context) {
        return this.defaultBlockState().setValue(DIR, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIR);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
        return state.setValue(DIR, direction.rotate(state.getValue(DIR)));
    }

    private static int getSlot(Direction dir, Vec3 hit) {
        hit = new Vec3(
                ((hit.x < 0) ? hit.x - Math.floor(hit.x) : hit.x) % 1,
                ((hit.y < 0) ? hit.y - Math.floor(hit.y) : hit.y) % 1,
                ((hit.z < 0) ? hit.z - Math.floor(hit.z) : hit.z) % 1
        );
        double x, z;
        x = dir.getAxis() == Direction.Axis.X ? (dir.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 1-hit.x() : hit.x()) : (dir.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 1-hit.z() : hit.z());
        z = dir.getAxis() == Direction.Axis.X ? (dir.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 1-hit.z() : hit.z()) : (dir.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? hit.x() : 1-hit.x());
        int slotIndex = 0;
        if (x <= 0.25)
            slotIndex += 9;
        else if (x <= 0.5)
            slotIndex += 6;
        else if (x <= 0.75)
            slotIndex += 3;
        return slotIndex + Mth.clamp(Mth.floor(z*3), 0, 2) + 1;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (world.getBlockEntity(pos) instanceof CardStandBlockEntity)
            Containers.dropContents(world, pos, ((CardStandBlockEntity) (world.getBlockEntity(pos))).getInventory());
        return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof CardStandBlockEntity cardStand) {
            return cardStand.getCardsAmt();
        }
        return 0;
    }
}
