package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.block.GraderBlock;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.item.CreativeGradingSleeveItem;
import com.wildcard.buddycards.item.GradingSleeveItem;
import com.wildcard.buddycards.menu.GraderMenu;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GraderBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer {
    private final ItemStackHandler inventory = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getItem() instanceof BuddycardItem;
                case 1 -> stack.getItem() instanceof GradingSleeveItem;
                default -> false;
            };
        }
    };

    protected final ContainerData data;

    private LazyOptional<IItemHandler> lazyInventory = LazyOptional.empty();
    private int progress;

    public GraderBlockEntity(BlockPos pos, BlockState state) {
        super(BuddycardsEntities.GRADER_ENTITY.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return progress;
            }

            @Override
            public void set(int index, int value) {
                progress = value;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block." + Buddycards.MOD_ID + ".grader");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new GraderMenu(id, inventory, this, this.data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyInventory = LazyOptional.of(() -> inventory);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyInventory.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        compound.put("inventory", inventory.serializeNBT());
        compound.putInt("progress", progress);
        super.saveAdditional(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        inventory.deserializeNBT(compound.getCompound("inventory"));
        progress = compound.getInt("progress");
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GraderBlockEntity entity) {
        if(canGrade(entity)) {
            entity.progress++;
            if(entity.progress > 28)
                performGrade(entity);
            setChanged(level, pos, state);
            if (!state.getValue(GraderBlock.GRADING)) {
                state = state.setValue(GraderBlock.GRADING, true);
                level.setBlock(pos, state, 3);
            }
        }
        else if (entity.progress != 0) {
            entity.progress = 0;
            state = state.setValue(GraderBlock.GRADING, false);
            setChanged(level, pos, state);
            level.setBlock(pos, state, 3);
        }
    }

    private static boolean canGrade(GraderBlockEntity entity) {
        boolean flag = false;
        for (int i = 2; i < 7; i++)
            if (entity.inventory.getStackInSlot(i).isEmpty()) {
                flag = true;
                break;
            }
        if (!flag)
            return false;
        if (entity.inventory.getStackInSlot(0).getItem() instanceof BuddycardItem && entity.inventory.getStackInSlot(1).getItem() instanceof GradingSleeveItem)
            return entity.inventory.getStackInSlot(0).getTag() == null || !entity.inventory.getStackInSlot(0).getTag().contains("grade");
        return false;
    }

    private static void performGrade(GraderBlockEntity entity) {
        float[] odds = ((GradingSleeveItem) entity.inventory.getStackInSlot(1).getItem()).ODDS;
        if (!(entity.getInventory().getStackInSlot(1).getItem() instanceof CreativeGradingSleeveItem))
            entity.getInventory().getStackInSlot(1).shrink(1);
        ItemStack output = entity.getInventory().getStackInSlot(0).split(1);
        CompoundTag tag = output.getOrCreateTag();
        int grade;
        float rand = entity.level.getRandom().nextFloat();
        for (grade = 1; grade < 5; grade++) {
            if(rand < odds[grade-1])
                break;
            rand -= odds[grade-1];
        }
        tag.putInt("grade", grade);
        output.setTag(tag);
        for (int i = 2; i < 7; i++)
            if (entity.inventory.getStackInSlot(i).isEmpty()) {
                entity.inventory.setStackInSlot(i, output);
                break;
            } else if (entity.inventory.getStackInSlot(i).getItem().equals(output.getItem()) &&
                    entity.inventory.getStackInSlot(i).getCount() < 64 &&
                    entity.inventory.getStackInSlot(i).hasTag() &&
                    entity.inventory.getStackInSlot(i).getTag().equals(tag)) {
                entity.inventory.getStackInSlot(i).grow(1);
                break;
            }
        entity.progress = 0;
    }

    @Override
    public int[] getSlotsForFace(Direction dir) {
        if (!dir.equals(Direction.DOWN))
            return new int[] {0, 1};
        else return new int[] {2, 3, 4, 5, 6};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction dir) {
        return index < 2 && inventory.isItemValid(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction dir) {
        return index > 1;
    }

    @Override
    public int getContainerSize() {
        return 7;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < 7; i++) {
           if(!inventory.getStackInSlot(i).equals(ItemStack.EMPTY))
               return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int amt) {
        return inventory.extractItem(index, amt, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return inventory.extractItem(index, inventory.getStackInSlot(index).getCount(), false);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        inventory.setStackInSlot(index, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < 7; i++)
            inventory.setStackInSlot(i, ItemStack.EMPTY);
    }
}
