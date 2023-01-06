package com.wildcard.buddycards.block;

import com.wildcard.buddycards.block.entity.PlaymatBlockEntity;
import com.wildcard.buddycards.container.BattleContainer;
import com.wildcard.buddycards.item.DeckboxItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlaymatBlock extends BaseEntityBlock {
    public static final DirectionProperty DIR = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);

    public PlaymatBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlaymatBlockEntity(pos, state);
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

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        //If both playmats exist...
        if(level.getBlockEntity(pos) instanceof PlaymatBlockEntity entity && level.getBlockEntity(pos.relative(state.getValue(DIR))) instanceof PlaymatBlockEntity opponent) {
            //If both have no container, set them up
            if (entity.container == null && opponent.container == null) {
                entity.container = new BattleContainer();
                entity.container.entity = entity;
                opponent.container = entity.container;
                entity.p1 = true;
                opponent.p1 = false;
                entity.setChanged();
                opponent.setChanged();
            }
            //If one has a container, make them match
            else if (entity.container == null || opponent.container == null) {
                if(entity.p1) {
                    opponent.container = entity.container;
                    opponent.setChanged();
                }
                else {
                    entity.container = opponent.container;
                    entity.setChanged();
                }
            }
            //If you are holding a deckbox, swap it out
            if (player.getItemInHand(hand).getItem() instanceof DeckboxItem && !entity.inGame) {
                player.setItemInHand(hand, entity.swapDeck(player.getItemInHand(hand)));
            }
            //If there's a container and both decks
            if (entity.container != null && entity.getContainer().getItem(0).getItem() instanceof DeckboxItem && entity.getContainer().getItem(7).getItem() instanceof DeckboxItem) {
                //If a game isn't started, start one
                if(!entity.inGame) {
                    entity.container.startGame();
                    entity.inGame = true;
                    opponent.inGame = true;
                    entity.setChanged();
                    opponent.setChanged();
                }
                //Open the GUI
                player.openMenu(entity);
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }
}
