package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.container.PlaymatContainer;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class PlaymatBlockEntity extends BlockEntity implements MenuProvider {
    private LazyOptional<ItemStackHandler> handler = LazyOptional.of(() -> new ItemStackHandler(7));
    private Component name;
    private PlaymatBlockEntity opponent;

    public PlaymatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public PlaymatBlockEntity(BlockPos pos, BlockState state) {
        super(BuddycardsEntities.BATTLE_BOARD_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        if (name != null)
            return name;
        return new TranslatableComponent("block." + Buddycards.MOD_ID + ".battle_board");
    }

    public void setDisplayName(Component nameIn) {
        name = nameIn;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new PlaymatContainer(i, inventory, this.worldPosition);
    }

    public IItemHandler getHandler() {
        return handler.orElse(new ItemStackHandler(7));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if(name != null)
            tag.putString("name", Component.Serializer.toJson(name));
        this.handler.ifPresent((stack) -> tag.put("inv", stack.serializeNBT()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("name"))
            this.name = Component.Serializer.fromJson(tag.getString("name"));
        CompoundTag invTag = tag.getCompound("inv");
        this.handler.ifPresent((stack) -> stack.deserializeNBT(invTag));
    }

    public PlaymatBlockEntity getOpponent() {
        return opponent;
    }

    public void setOpponent(PlaymatBlockEntity opponent) {
        this.opponent = opponent;
    }
}
