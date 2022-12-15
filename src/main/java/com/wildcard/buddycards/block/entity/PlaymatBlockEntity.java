package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.block.PlaymatBlock;
import com.wildcard.buddycards.container.BattleContainer;
import com.wildcard.buddycards.menu.PlaymatMenu;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PlaymatBlockEntity extends BlockEntity implements MenuProvider {
    public BattleContainer container = new BattleContainer();
    public boolean isPaired;
    public boolean p1;
    private Component name;

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
        return new TextComponent("empty");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new PlaymatMenu(i, inventory, container);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if(name != null)
            tag.putString("name", Component.Serializer.toJson(name));
        tag.putBoolean("paired", isPaired);
        tag.putBoolean("p1", p1);
        if(p1) {
            CompoundTag log = new CompoundTag();
            for (int i = 0; i < container.battleLog.size(); i++) {
                log.putInt(Component.Serializer.toJson(container.battleLog.get(i)), i);
            }
            tag.put("log", log);
            tag.put("inv", this.container.createTag());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("name"))
            this.name = Component.Serializer.fromJson(tag.getString("name"));
        isPaired = tag.getBoolean("paired");
        p1 = tag.getBoolean("p1");
        if(p1 && tag.contains("inv")) {
            container.fromTag(tag.getList("inv", Tag.TAG_LIST));
            container.reload();
            if (container.battleLog.isEmpty() && tag.contains("log")) {
                CompoundTag log = (CompoundTag) tag.get("log");
                for (String i: log.getAllKeys()) {
                    container.battleLog.add(log.getInt(i), Component.Serializer.fromJson(i));
                }
            }
        }
        if (isPaired && level.getBlockEntity(getBlockPos().relative(level.getBlockState(getBlockPos()).getValue(PlaymatBlock.DIR))) instanceof PlaymatBlockEntity opponent) {
            if (p1)
                opponent.container = container;
            else
                container = opponent.getContainer();
        }
    }

    public BattleContainer getContainer() {
        return container;
    }

    public ItemStack swapDeck(ItemStack itemInHand) {
        System.out.println("Swapping deck...");
        ItemStack removedDeck = container.getItem(p1 ? 0 : 7);
        container.setItem(p1 ? 0 : 7, itemInHand);
        name = itemInHand.getDisplayName();
        setChanged();
        return removedDeck;
    }
}
