package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.battles.BuddycardBattle;
import com.wildcard.buddycards.container.PlaymatContainer;
import com.wildcard.buddycards.menu.PlaymatMenu;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
    private final PlaymatContainer container = new PlaymatContainer();
    private Component name;
    public BuddycardBattle battle;

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
        return new PlaymatMenu(i, inventory, container);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if(name != null)
            tag.putString("name", Component.Serializer.toJson(name));
        tag.put("inv", this.container.createTag());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("name"))
            this.name = Component.Serializer.fromJson(tag.getString("name"));
        container.fromTag(tag.getList("inv", Tag.TAG_LIST));
    }

    public PlaymatContainer getContainer() {
        return container;
    }

    public ItemStack swapDeck(ItemStack itemInHand) {
        System.out.println("Swapping deck...");
        ItemStack removedDeck = container.getItem(0);
        container.setDeckbox(itemInHand);
        return removedDeck;
    }
}
