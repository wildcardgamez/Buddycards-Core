package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.util.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KineticChamberBlockEntity extends BlockEntity implements Clearable {
    private ItemStack itemSlot = ItemStack.EMPTY;

    public KineticChamberBlockEntity(BlockPos pos, BlockState state) {
        super(BuddycardsEntities.KINETIC_CHAMBER_ENTITY.get(), pos, state);
    }

    public KineticChamberBlockEntity(BlockPos pos, BlockState state, BlockEntityType block) {
        super(block, pos, state);
    }

    @Override
    public void clearContent() {
        itemSlot = ItemStack.EMPTY;
    }

    public ItemStack getItemSlot() {
        return itemSlot;
    }

    public void setItemSlot(ItemStack stack) {
        if(this.level != null) {
            itemSlot = stack;
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void absorbExplosion(ServerLevel lvl) {
        if(lvl.random.nextFloat() < ConfigManager.kineticSuccessRate.get()) {
            if(itemSlot.getItem().equals(BuddycardsItems.CRIMSON_LUMINIS_BLOCK.get()) && lvl.random.nextFloat() < ConfigManager.luminisKineticSpecialtyOdds.get()) {
                LootTable table = lvl.getServer().getLootData().getLootTable(new ResourceLocation(Buddycards.MOD_ID, "gameplay/luminis_kinetic_chamber"));
                List<ItemStack> items = table.getRandomItems((new LootParams.Builder(lvl)).create(LootContextParamSets.EMPTY));
                itemSlot = ItemStack.EMPTY;
                for (ItemStack i: items)
                    if (!i.isEmpty())
                        itemSlot = i;
            }
            else if(itemSlot.getItem().equals(BuddycardsItems.LUMINIS_BLOCK.get()) && lvl.random.nextFloat() < ConfigManager.luminisKineticCrimsonOdds.get()) {
                itemSlot = new ItemStack(BuddycardsItems.CRIMSON_LUMINIS.get());
            }
            else
                return;
        }
        this.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("item", itemSlot.save(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if(compound.contains("item"))
            itemSlot = ItemStack.of(compound.getCompound("item"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if(level instanceof ServerLevel serverLevel)
            serverLevel.getChunkSource().blockChanged(getBlockPos());
    }
}