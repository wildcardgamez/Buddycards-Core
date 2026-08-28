package com.wildcard.buddycards.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.wildcard.buddycards.block.entity.CardStandBlockEntity;
import com.wildcard.buddycards.core.CardInfo;
import com.wildcard.buddycards.core.CardInfoProviderBlock;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
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
import java.util.stream.Stream;

public class CardStandBlock extends BaseEntityBlock implements CardInfoProviderBlock {
    public static final DirectionProperty DIR = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty COVERED = BooleanProperty.create("covered");
    private static final Map<Direction, VoxelShape> SHAPES = Util.make(() -> {
        Map<Direction, VoxelShape> shape = new HashMap<>();
        shape.put(Direction.NORTH, Shapes.or(Block.box(0, 0, 12, 16, 2, 16), Block.box(0, 0, 8, 16, 4, 12), Block.box(0, 0, 4, 16, 6, 8), Block.box(0, 0, 0, 16, 8, 4)));
        shape.put(Direction.EAST, Shapes.or(Block.box(0, 0, 0, 4, 2, 16), Block.box(4, 0, 0, 8, 4, 16), Block.box(8, 0, 0, 12, 6, 16), Block.box(12, 0, 0, 16, 8, 16)));
        shape.put(Direction.SOUTH, Shapes.or(Block.box(0, 0, 0, 16, 2, 4), Block.box(0, 0, 4, 16, 4, 8), Block.box(0, 0, 8, 16, 6, 12), Block.box(0, 0, 12, 16, 8, 16)));
        shape.put(Direction.WEST, Shapes.or(Block.box(12, 0, 0, 16, 2, 16), Block.box(8, 0, 0, 12, 4, 16), Block.box(4, 0, 0, 8, 6, 16), Block.box(0, 0, 0, 4, 8, 16)));
        return ImmutableMap.copyOf(shape);
    });
    public static final MapCodec<CardStandBlock> CODEC = simpleCodec(CardStandBlock::new);

    public CardStandBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof CardStandBlockEntity standEntity && level instanceof ServerLevel) {
            if (standEntity.isLocked())
                return InteractionResult.PASS;
            if (!standEntity.getGlass().isEmpty()) {
                ItemStack glass = standEntity.getGlass();
                standEntity.putGlass(ItemStack.EMPTY);
                if(!player.addItem(glass))
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), glass);
            }
            else {
                int slot = getSlot(state.getValue(DIR), hitResult.getLocation());
                if (standEntity.getCardInSlot(slot).getItem() instanceof BuddycardItem) {
                    ItemStack oldCard = standEntity.getCardInSlot(slot);
                    standEntity.putCardInSlot(ItemStack.EMPTY, slot);
                    if (!player.addItem(oldCard))
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), oldCard);
                }
            }
        }
        level.updateNeighbourForOutputSignal(pos, this);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof CardStandBlockEntity standEntity && level instanceof ServerLevel) {
            if (standEntity.isLocked())
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            if (!standEntity.getGlass().isEmpty()) {
                ItemStack glass = standEntity.getGlass();
                standEntity.putGlass(ItemStack.EMPTY);
                if (!player.addItem(glass))
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), glass);
            } else if (stack.getItem().equals(Items.GLASS))
                standEntity.putGlass(stack.split(1));
            else {
                int slot = getSlot(state.getValue(DIR), hitResult.getLocation());
                if (standEntity.getCardInSlot(slot).getItem() instanceof BuddycardItem) {
                    {
                        ItemStack oldCard = standEntity.getCardInSlot(slot);
                        if (stack.getItem() instanceof BuddycardItem)
                            standEntity.putCardInSlot(stack.split(1), slot);
                        else
                            standEntity.putCardInSlot(ItemStack.EMPTY, slot);
                        if (!player.addItem(oldCard))
                            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), oldCard);
                    }
                } else if (stack.getItem() instanceof BuddycardItem)
                    standEntity.putCardInSlot(stack.split(1), slot);
            }
        }
        level.updateNeighbourForOutputSignal(pos, this);
        return ItemInteractionResult.SUCCESS;
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
        if (state.getValue(COVERED))
            return Shapes.block();
        return SHAPES.get(state.getValue(DIR));
    }

    @Override
    public BlockState getStateForPlacement (BlockPlaceContext context) {
        return this.defaultBlockState().setValue(DIR, context.getHorizontalDirection()).setValue(COVERED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIR).add(COVERED);
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
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level.getBlockEntity(pos) instanceof CardStandBlockEntity)
            Containers.dropContents(level, pos, ((CardStandBlockEntity) (level.getBlockEntity(pos))).getInventory());
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CardStandBlockEntity cardStand) {
            return cardStand.getCardsAmt();
        }
        return 0;
    }

    @Override
    public Stream<CardInfo> getAllCardInfo(BlockState blockState, Level world, BlockPos pos, Player player) {
        if (world.getBlockEntity(pos) instanceof CardStandBlockEntity cardStand && (!cardStand.isLocked() || cardStand.playerHasAccess(player.getUUID()))) {
            return cardStand.getCardInfo();
        }
        return Stream.empty();
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPES.get(state.getValue(DIR));
    }
}
