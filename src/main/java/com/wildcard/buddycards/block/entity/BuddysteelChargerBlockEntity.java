package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.CardInfo;
import com.wildcard.buddycards.item.BuddysteelScannerItem;
import com.wildcard.buddycards.item.BuddysteelSetMedalItem;
import com.wildcard.buddycards.menu.ChargerMenu;
import com.wildcard.buddycards.recipe.BuddysteelChargingRecipe;
import com.wildcard.buddycards.recipe.BuddysteelChargingRecipeInput;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class BuddysteelChargerBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer {
    private final ItemStackHandler inventory = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 5 -> stack.getItem() instanceof BuddysteelScannerItem;
                case 6 -> false;
                default -> true;
            };
        }
    };

    protected final ContainerData data;

    private int progress = 0, maxProgress = 72;

    public BuddysteelChargerBlockEntity(BlockPos pos, BlockState blockState) {
        super(BuddycardsEntities.CHARGER_ENTITY.get(), pos, blockState);
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
        return Component.translatable("block." + Buddycards.MOD_ID + ".buddysteel_charger");
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (!direction.equals(Direction.DOWN))
            return new int[] {6};
        else return new int[] {0, 1, 2, 3, 4};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return index < 5 && inventory.isItemValid(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
        return index == 6;
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

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ChargerMenu(i, inventory, this, this.data);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BuddysteelChargerBlockEntity entity) {
        if(hasRecipe(entity) && (entity.progress > 0 || hasCompletion(entity))) {
            entity.progress++;
            setChanged(level, pos, state);
            if (entity.progress > entity.maxProgress)
                if (entity.inventory.getStackInSlot(0).getItem() instanceof BuddysteelSetMedalItem medal) {
                    ItemStack stack = entity.inventory.extractItem(0, 1, false);
                    medal.updateMedal(stack, entity.inventory.getStackInSlot(5), (ServerLevel) level);
                    entity.inventory.setStackInSlot(6, stack);
                    entity.progress = 0;
                    entity.setChanged();
                } else
                    craftItem(entity);
        }
        else if (entity.progress != 0) {
            entity.progress = 0;
            setChanged(level, pos, state);
        }
    }

    private static void craftItem(BuddysteelChargerBlockEntity entity) {
        Optional<RecipeHolder<BuddysteelChargingRecipe>> recipe = entity.currentRecipe();
        for (int i = 0; i < 5; i++) {
            entity.inventory.extractItem(i, 1, false);
        }
        ItemStack output = recipe.get().value().getResultItem();
        if (output.getItem() instanceof BuddysteelSetMedalItem medal) {
            medal.initializeMedal(output, (ServerLevel) entity.level);
            medal.updateMedal(output, entity.inventory.getStackInSlot(5), (ServerLevel) entity.level);
            entity.inventory.setStackInSlot(6, output);
        }
        else
            entity.inventory.setStackInSlot(6, new ItemStack(output.getItem(), entity.inventory.getStackInSlot(6).getCount() + output.getCount()));
        entity.progress = 0;
        entity.setChanged();
    }

    private static boolean hasRecipe(BuddysteelChargerBlockEntity entity) {
        SimpleContainer inv = new SimpleContainer(entity.inventory.getSlots());
        for (int i = 0; i < entity.inventory.getSlots(); i++)
            inv.setItem(i, entity.inventory.getStackInSlot(i));
        Optional<RecipeHolder<BuddysteelChargingRecipe>> recipe = entity.currentRecipe();
        ItemStack scanner = entity.inventory.getStackInSlot(5), output = entity.inventory.getStackInSlot(6);
        return recipe.isPresent() && scanner.getItem() instanceof BuddysteelScannerItem scannerItem &&
                (output.isEmpty() || (output.getItem().equals(recipe.get().value().getResultItem().getItem())
                        && output.getCount() + recipe.get().value().getResultItem().getCount() <= output.getMaxStackSize()));
    }

    private static boolean hasCompletion(BuddysteelChargerBlockEntity entity) {
        ItemStack scanner = entity.inventory.getStackInSlot(5);
        Optional<RecipeHolder<BuddysteelChargingRecipe>> recipe = entity.currentRecipe();
        return scanner.getItem() instanceof BuddysteelScannerItem scannerItem && scannerItem.getCompletionPercentageForSet(scanner, recipe.get().value().getTier(), recipe.get().value().getSet()) >= recipe.get().value().getPercentage();
    }

    private Optional<RecipeHolder<BuddysteelChargingRecipe>> currentRecipe() {
        NonNullList<ItemStack> ingredients = NonNullList.withSize(4, ItemStack.EMPTY);
        for (int i = 0; i < 4; i++)
            ingredients.set(i, inventory.getStackInSlot(i+1));
        return level.getRecipeManager().getRecipeFor(BuddycardsMisc.CHARGING_RECIPE.get(), new BuddysteelChargingRecipeInput(inventory.getStackInSlot(0), ingredients), level);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        compound.put("inventory", inventory.serializeNBT(registries));
        compound.putInt("progress", progress);
        super.saveAdditional(compound, registries);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        inventory.deserializeNBT(registries, compound.getCompound("inventory"));
        progress = compound.getInt("progress");
    }
}
