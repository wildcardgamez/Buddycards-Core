package com.wildcard.buddycards.block;

<<<<<<< Updated upstream:src/main/java/com/wildcard/buddycards/block/BattleBoardBlock.java
import com.wildcard.buddycards.block.entity.BattleBoardBlockEntity;
=======
import com.wildcard.buddycards.block.entity.PlaymatBlockEntity;
>>>>>>> Stashed changes:src/main/java/com/wildcard/buddycards/block/PlaymatBlock.java
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
<<<<<<< Updated upstream:src/main/java/com/wildcard/buddycards/block/BattleBoardBlock.java
=======
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
>>>>>>> Stashed changes:src/main/java/com/wildcard/buddycards/block/PlaymatBlock.java
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
<<<<<<< Updated upstream:src/main/java/com/wildcard/buddycards/block/BattleBoardBlock.java
=======
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
>>>>>>> Stashed changes:src/main/java/com/wildcard/buddycards/block/PlaymatBlock.java

public class BattleBoardBlock extends BaseEntityBlock {
    public static final DirectionProperty DIR = BlockStateProperties.HORIZONTAL_FACING;

    public BattleBoardBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BattleBoardBlockEntity(pos, state);
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
<<<<<<< Updated upstream:src/main/java/com/wildcard/buddycards/block/BattleBoardBlock.java
=======

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState, BlockState newState) {
        if(level.getBlockEntity(pos.relative(newState.getValue(DIR))) instanceof PlaymatBlockEntity opponent) {
            ((PlaymatBlockEntity) level.getBlockEntity(pos)).setOpponent(opponent);
            opponent.setOpponent((PlaymatBlockEntity) level.getBlockEntity(pos));
            System.out.println("Opponent linked!");
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof PlaymatBlockEntity entity) {
            player.openMenu(entity);
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }
>>>>>>> Stashed changes:src/main/java/com/wildcard/buddycards/block/PlaymatBlock.java
}
