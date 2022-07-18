package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CardDisplayBlockEntity extends BlockEntity implements Clearable {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);

    public CardDisplayBlockEntity(BlockPos pos, BlockState state) {
        super(BuddycardsEntities.CARD_DISPLAY_TILE.get(), pos, state);
    }

    public CardDisplayBlockEntity(BlockPos pos, BlockState state, BlockEntityType block) {
        super(block, pos, state);
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

    public int getCardsAmt() {
        int amt = 0;
        for (int i = 0; i < 6; i++) {
            if (this.inventory.get(i).getItem() instanceof BuddycardItem)
                amt++;
        }
        return amt;
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        CompoundTag nbt = new CompoundTag();
        ContainerHelper.saveAllItems(nbt, this.inventory, true);
        compound.put("cards", nbt);
        super.saveAdditional(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.inventory.clear();
        ContainerHelper.loadAllItems(compound.getCompound("cards"), this.inventory);

    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return nbt;
    }

    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }
}