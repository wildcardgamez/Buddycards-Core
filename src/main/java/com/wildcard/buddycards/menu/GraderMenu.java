package com.wildcard.buddycards.menu;

import com.wildcard.buddycards.block.entity.GraderBlockEntity;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.item.GradingSleeveItem;
import com.wildcard.buddycards.registries.BuddycardsBlocks;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class GraderMenu  extends AbstractContainerMenu {
    private final GraderBlockEntity entity;
    private final Level level;
    private final ContainerData data;

    public GraderMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, playerInv.player.level().getBlockEntity(buf.readBlockPos()), new SimpleContainerData(2));
    }

    public GraderMenu(int id, Inventory playerInv, BlockEntity entity, ContainerData data) {
        super(BuddycardsMisc.GRADER_MENU.get(), id);
        checkContainerSize(playerInv, 7);
        this.entity = (GraderBlockEntity) entity;
        this.level = playerInv.player.level();
        this.data = data;
        IItemHandler handler = ((GraderBlockEntity) entity).getInventory();
        if(handler != null) {
            //set up input slot
            this.addSlot(new InputSlot(handler, 0, 62, 18));
            //set up sleeve slot
            this.addSlot(new SleeveSlot(handler, 1, 98, 18));
            //Set up output slots
            for (int i = 0; i < 5; i++)
                this.addSlot(new OutputSlot(handler, 2 + i, 44 + i * 18, 54));
        }
        //Set up slots for inventory
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInv, x + (y * 9) + 9, 8 + x * 18, 86 + y * 18));
            }
        }
        //Set up slots for hotbar
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInv, x, 8 + x * 18, 144));
        }

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), player, BuddycardsBlocks.GRADER.get());
    }

    public class InputSlot extends SlotItemHandler {
        public InputSlot(IItemHandler handler, int index, int xPosition, int yPosition) {
            super(handler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof BuddycardItem;
        }
    }

    public class SleeveSlot extends SlotItemHandler {
        public SleeveSlot(IItemHandler handler, int index, int xPosition, int yPosition) {
            super(handler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof GradingSleeveItem;
        }
    }

    public class OutputSlot extends SlotItemHandler {
        public OutputSlot(IItemHandler handler, int index, int xPosition, int yPosition) {
            super(handler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if(slot.hasItem())
        {
            stack = slot.getItem().copy();
            if (index < slots.size() - 36)
            {
                if(!this.moveItemStackTo(slot.getItem(), slots.size() - 36, slots.size(), true))
                    return ItemStack.EMPTY;
            }
            else if(!this.moveItemStackTo(slot.getItem(), 0, slots.size() - 36, false))
                return ItemStack.EMPTY;
            if(slot.getItem().isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
        }
        return stack;
    }

    public int getProgress() {
        return data.get(0);
    }
}
