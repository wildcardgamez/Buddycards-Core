package com.wildcard.buddycards.block;

import com.wildcard.buddycards.block.entity.PlaymatBlockEntity;
import com.wildcard.buddycards.container.BattleContainer;
import com.wildcard.buddycards.item.DeckboxItem;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
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
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;

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
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean notMoveByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof PlaymatBlockEntity playmat) {
            Optional<PlaymatBlockEntity> blockEntity = level.getBlockEntity(pos.relative(state.getValue(DIR)), BuddycardsEntities.PLAYMAT_ENTITY.get());
            playmat.inGame = false;
            dropPlayMat(playmat, pos, level);
            blockEntity.ifPresent(be -> dropPlayMat(be, pos, level));
        }
        super.onRemove(state, level, pos, newState, notMoveByPiston);
    }

    private static void dropPlayMat(PlaymatBlockEntity playmat, BlockPos pos, Level level) {

        if (playmat.container != null) {
            Containers.dropContents(level, pos, new SimpleContainer(playmat.container.getItem(0)));
            Containers.dropContents(level, pos, new SimpleContainer(playmat.container.getItem(7)));
            playmat.container = null;
            playmat.inGame = false;
            playmat.p1 = false;
        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIR);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {

        return state.setValue(DIR, direction.rotate(state.getValue(DIR)));
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
        if(level.getBlockEntity(pos) instanceof PlaymatBlockEntity self
                && level.getBlockEntity(pos.relative(state.getValue(DIR))) instanceof PlaymatBlockEntity opponent
                && opponent.getBlockState().hasProperty(DIR)
                && opponent.getBlockState().getValue(DIR).getOpposite() == state.getValue(DIR)) {

            //If both have no container, set them up
            if (self.container == null && opponent.container == null) {
                self.container = new BattleContainer();
                self.container.entity = self;
                opponent.container = self.container;
                self.p1 = true;
                opponent.p1 = false;
                self.setChanged();
                opponent.setChanged();
            }
            //If one has a container, make them match
            else if (self.container == null || opponent.container == null) {
                if(self.p1) {
                    opponent.container = self.container;
                    opponent.setChanged();
                }
                else {
                    self.container = opponent.container;
                    self.setChanged();
                }
            }
            //If you are holding a deckbox, swap it out
            if (player.getItemInHand(hand).getItem() instanceof DeckboxItem && !self.inGame) {
                player.setItemInHand(hand, self.swapDeck(player.getItemInHand(hand)));
            }
            //If there's a container and both decks
            if (self.container != null && self.getContainer().getItem(0).getItem() instanceof DeckboxItem && self.getContainer().getItem(7).getItem() instanceof DeckboxItem) {
                //If a game isn't started, start one
                if(!self.inGame) {
                    self.container.startGame();
                    self.inGame = true;
                    opponent.inGame = true;
                    self.setChanged();
                    opponent.setChanged();
                }
                //Open the GUI
                if (player instanceof ServerPlayer serverPlayer) NetworkHooks.openGui(serverPlayer, self, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }
}
