package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class KineticChamberBlockEntity extends BlockEntity implements Clearable {
    private ItemStack itemSlot = ItemStack.EMPTY;

    private static final ResourceLocation specialtyLootTable = new ResourceLocation(Buddycards.MOD_ID, "specialty/luminis");

    public KineticChamberBlockEntity(BlockPos pos, BlockState state) {
        super(BuddycardsEntities.KINETIC_CHAMBER_TILE.get(), pos, state);
    }

    public KineticChamberBlockEntity(BlockPos pos, BlockState state, BlockEntityType block) {
        super(block, pos, state);
    }

    @Override
    public void clearContent() {
        itemSlot = ItemStack.EMPTY;
    }

    public ItemStack takeItem() {
        if (!itemSlot.isEmpty()) {
            ItemStack item = itemSlot.copy();
            itemSlot = ItemStack.EMPTY;
            this.setChanged();
            return item;
        }
        return ItemStack.EMPTY;
    }

    public boolean insertItem(ItemStack item) {
        if (itemSlot.isEmpty()) {
            itemSlot = item;
            this.setChanged();
            return true;
        }
        return false;
    }

    public boolean hasItem() {
        return !itemSlot.isEmpty();
    }

    public void absorbExplosion(ServerLevel lvl) {
        if(lvl.random.nextFloat() < .1) {
            if(itemSlot.getItem().equals(BuddycardsItems.CRIMSON_LUMINIS_BLOCK.get())) {
                LootContext.Builder builder = (new LootContext.Builder(lvl).withRandom(lvl.random));
                LootTable table = lvl.getServer().getLootTables().get(specialtyLootTable);
                itemSlot = table.getRandomItems(builder.create(LootContextParamSets.EMPTY)).get(0);
            }
            else if(itemSlot.getItem().equals(BuddycardsItems.LUMINIS_BLOCK.get()) && lvl.random.nextFloat() < .75) {
                itemSlot = new ItemStack(BuddycardsItems.CRIMSON_LUMINIS.get());
            }
            else {
                if(itemSlot.isEmpty())
                    return;
                itemSlot = ItemStack.EMPTY;
            }
            this.setChanged();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("item", itemSlot.getTag());
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if(compound.contains("item"))
            itemSlot = ItemStack.of((CompoundTag) compound.get("item"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return nbt;
    }
}