package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.ChargerMenu;
import com.wildcard.buddycards.recipe.BuddysteelChargingRecipe;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BuddysteelChargerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler inventory = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    protected final ContainerData data;

    private LazyOptional<IItemHandler> lazyInventory = LazyOptional.empty();
    private int progress = 0;
    private int maxProgress = 72;

    public BuddysteelChargerBlockEntity(BlockPos pos, BlockState state) {
        super(BuddycardsEntities.BUDDYSTEEL_CHARGER_TILE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BuddysteelChargerBlockEntity.this.progress;
                    case 1 -> BuddysteelChargerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        BuddysteelChargerBlockEntity.this.progress = value;
                        break;
                    case 1:
                        BuddysteelChargerBlockEntity.this.maxProgress = value;
                        break;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block." + Buddycards.MOD_ID + ".buddysteel_charger");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ChargerMenu(id, inventory, this, this.data);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return lazyInventory.cast();
        return super.getCapability(cap);
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

    public static void tick(Level level, BlockPos pos, BlockState state, BuddysteelChargerBlockEntity entity) {
        if(hasRecipe(entity)) {
            entity.progress++;
            setChanged(level, pos, state);
            if(entity.progress > entity.maxProgress)
                craftItem(entity);
        }
        else if (entity.progress != 0) {
            entity.progress = 0;
            setChanged(level, pos, state);
        }
    }

    private static void craftItem(BuddysteelChargerBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inv = new SimpleContainer(entity.inventory.getSlots());
        for (int i = 0; i < entity.inventory.getSlots(); i++)
            inv.setItem(i, entity.inventory.getStackInSlot(i));
        Optional<BuddysteelChargingRecipe> recipe = level.getRecipeManager().getRecipeFor(BuddysteelChargingRecipe.Type.INSTANCE, inv, level);
        for (int i = 0; i < 5; i++) {
            entity.inventory.extractItem(i, 1, false);
        }
        ItemStack output = recipe.get().getResultItem();
        entity.inventory.setStackInSlot(6, new ItemStack(output.getItem(), entity.inventory.getStackInSlot(6).getCount() + output.getCount()));
        entity.progress = 0;
        entity.setChanged();
    }

    private static boolean hasRecipe(BuddysteelChargerBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inv = new SimpleContainer(entity.inventory.getSlots());
        for (int i = 0; i < entity.inventory.getSlots(); i++)
            inv.setItem(i, entity.inventory.getStackInSlot(i));
        Optional<BuddysteelChargingRecipe> recipe = level.getRecipeManager().getRecipeFor(BuddysteelChargingRecipe.Type.INSTANCE, inv, level);
        ItemStack meter = entity.inventory.getStackInSlot(5), output = entity.inventory.getStackInSlot(6);
        return recipe.isPresent() && meter.hasTag() && meter.getTag().getFloat("power") >= recipe.get().getPowerReq() &&
                (output.isEmpty() ||
                        (output.getItem().equals(recipe.get().getResultItem().getItem()) &&
                                output.getCount() + recipe.get().getResultItem().getCount() <= output.getMaxStackSize()));
    }
}
