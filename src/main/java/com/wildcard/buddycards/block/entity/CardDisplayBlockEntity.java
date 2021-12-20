package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CardDisplayBlockEntity extends BlockEntity implements Clearable {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);
    private String player = "";

    public CardDisplayBlockEntity(BlockPos pos, BlockState state) {
        super(BuddycardsEntities.CARD_DISPLAY_TILE.get(), pos, state);
    }

    public void putCardInSlot(ItemStack stack, int pos) {
        if(this.level != null) {
            this.inventory.set(pos - 1, stack);
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public ItemStack getCardInSlot(int pos) {
        return this.inventory.get(pos - 1);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, this.inventory, true);
        compound.putString("player", this.player);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.clear();
        ContainerHelper.loadAllItems(nbt, this.inventory);
        this.player = nbt.getString("player");
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(getLevel().getBlockEntity(worldPosition));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }

    public int getCardsAmt() {
        int amt = 0;
        for (int i = 0; i < 6; i++) {
            if (this.inventory.get(i).getItem() instanceof BuddycardItem)
                amt++;
        }
        return amt;
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }
}

