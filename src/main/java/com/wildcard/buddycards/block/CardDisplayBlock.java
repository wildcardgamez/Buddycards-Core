package com.wildcard.buddycards.block;

import com.wildcard.buddycards.block.entity.CardDisplayBlockEntity;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CardDisplayBlock extends BaseEntityBlock {
    public static final DirectionProperty DIR = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape NSHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape ESHAPE = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SSHAPE = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WSHAPE = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);

    public CardDisplayBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int slot = getSlot(state.getValue(DIR), hit.getLocation());
        if (world.getBlockEntity(pos) instanceof CardDisplayBlockEntity) {
            CardDisplayBlockEntity displayTile = (CardDisplayBlockEntity) world.getBlockEntity(pos);
            ItemStack stack = player.getItemInHand(hand);
            if(displayTile.getCardInSlot(slot).getItem() instanceof BuddycardItem) {
                ItemStack oldCard = displayTile.getCardInSlot(slot);
                if (stack.getItem() instanceof BuddycardItem) {
                    ItemStack card = new ItemStack(stack.getItem(), 1);
                    card.setTag(stack.getTag());
                    displayTile.putCardInSlot(card, slot);
                    stack.shrink(1);
                }
                else {
                    displayTile.putCardInSlot(ItemStack.EMPTY, slot);
                }
                if(!player.addItem(oldCard))
                    Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), oldCard);
            }
            else if(stack.getItem() instanceof BuddycardItem) {
                ItemStack card = new ItemStack(stack.getItem(), 1);
                card.setTag(stack.getTag());
                displayTile.putCardInSlot(card, slot);
                stack.shrink(1);
            }
        }
        world.updateNeighbourForOutputSignal(pos, this);
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CardDisplayBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(DIR);
        switch(direction) {
            case NORTH:
            default:
                return NSHAPE;
            case EAST:
                return ESHAPE;
            case SOUTH:
                return SSHAPE;
            case WEST:
                return WSHAPE;
        }
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
        switch(direction) {
            case CLOCKWISE_90:
                switch(state.getValue(DIR)) {
                    case NORTH:
                        return state.setValue(DIR, Direction.EAST);
                    case EAST:
                        return state.setValue(DIR, Direction.SOUTH);
                    case SOUTH:
                        return state.setValue(DIR, Direction.WEST);
                    case WEST:
                        return state.setValue(DIR, Direction.NORTH);
                }
            case CLOCKWISE_180:
                switch(state.getValue(DIR)) {
                    case NORTH:
                        return state.setValue(DIR, Direction.SOUTH);
                    case EAST:
                        return state.setValue(DIR, Direction.WEST);
                    case SOUTH:
                        return state.setValue(DIR, Direction.NORTH);
                    case WEST:
                        return state.setValue(DIR, Direction.EAST);
                }
            case COUNTERCLOCKWISE_90:
                switch(state.getValue(DIR)) {
                    case NORTH:
                        return state.setValue(DIR, Direction.WEST);
                    case EAST:
                        return state.setValue(DIR, Direction.NORTH);
                    case SOUTH:
                        return state.setValue(DIR, Direction.EAST);
                    case WEST:
                        return state.setValue(DIR, Direction.SOUTH);
                }
        }
        return state;
    }

    private int getSlot(Direction dir, Vec3 hit) {
        hit = new Vec3(
                ((hit.x < 0) ? hit.x - Math.floor(hit.x) : hit.x) % 1,
                ((hit.y < 0) ? hit.y - Math.floor(hit.y) : hit.y) % 1,
                ((hit.z < 0) ? hit.z - Math.floor(hit.z) : hit.z) % 1
        );
        if(hit.y > .5) {
            if(dir == Direction.NORTH) {
                if (hit.x < 1/3f)
                    return 1;
                else if (hit.x < 2/3f)
                    return 2;
                else
                    return 3;
            }
            if(dir == Direction.EAST) {
                if (hit.z < 1/3f)
                    return 1;
                else if (hit.z < 2/3f)
                    return 2;
                else
                    return 3;
            }
            if(dir == Direction.SOUTH) {
                if (hit.x > 2/3f)
                    return 1;
                else if (hit.x > 1/3f)
                    return 2;
                else
                    return 3;
            }
            if(dir == Direction.WEST) {
                if (hit.z > 2/3f)
                    return 1;
                else if (hit.z > 1/3f)
                    return 2;
                else
                    return 3;
            }
        }
        else {
            if(dir == Direction.NORTH) {
                if (hit.x < 1/3f)
                    return 4;
                else if (hit.x < 2/3f)
                    return 5;
                else
                    return 6;
            }
            if(dir == Direction.EAST) {
                if (hit.z < 1/3f)
                    return 4;
                else if (hit.z < 2/3f)
                    return 5;
                else
                    return 6;
            }
            if(dir == Direction.SOUTH) {
                if (hit.x > 2/3f)
                    return 4;
                else if (hit.x > 1/3f)
                    return 5;
                else
                    return 6;
            }
            if(dir == Direction.WEST) {
                if (hit.z > 2/3f)
                    return 4;
                else if (hit.z > 1/3f)
                    return 5;
                else
                    return 6;
            }
        }
        return 1;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (world.getBlockEntity(pos) instanceof CardDisplayBlockEntity)
            Containers.dropContents(world, pos, ((CardDisplayBlockEntity) (world.getBlockEntity(pos))).getInventory());
        return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        BlockEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof CardDisplayBlockEntity) {
            return ((CardDisplayBlockEntity) tileentity).getCardsAmt();
        }
        else
            return 0;
    }
}
