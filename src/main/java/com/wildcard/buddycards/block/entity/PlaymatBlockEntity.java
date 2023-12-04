package com.wildcard.buddycards.block.entity;

import com.wildcard.buddycards.battles.BattleComponent;
import com.wildcard.buddycards.battles.game.BattleCardState;
import com.wildcard.buddycards.block.PlaymatBlock;
import com.wildcard.buddycards.container.BattleContainer;
import com.wildcard.buddycards.container.DeckboxContainer;
import com.wildcard.buddycards.menu.PlaymatMenu;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
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
    public BattleContainer container;
    public boolean inGame;
    public boolean p1;
    private Component name;

    public PlaymatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public PlaymatBlockEntity(BlockPos pos, BlockState state) {
        super(BuddycardsEntities.PLAYMAT_ENTITY.get(), pos, state);
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
        return new PlaymatMenu(i, inventory, this);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if(name != null)
            tag.putString("name", Component.Serializer.toJson(name));
        tag.putBoolean("game", inGame);
        tag.putBoolean("p1", p1);
        if(p1) {
            tag.put("log", BattleComponent.LIST_CODEC.encodeStart(NbtOps.INSTANCE, container.battleLog).result().orElse(new ListTag()));
            CompoundTag inv = new CompoundTag();
            for (int i = 0; i < 14; i++) {
                inv.put("" + i, container.getItem(i).save(new CompoundTag()));
            }
            tag.put("inv", inv);
            if(container.deck1 != null) {
                CompoundTag deck1 = new CompoundTag();
                for (int i = 0; i < 16; i++) {
                    deck1.put("" + i, container.deck1.getItem(i).save(new CompoundTag()));
                }
                tag.put("deck1", deck1);
            }
            if(container.deck2 != null) {
                CompoundTag deck2 = new CompoundTag();
                for (int i = 0; i < 16; i++) {
                    deck2.put("" + i, container.deck2.getItem(i).save(new CompoundTag()));
                }
                tag.put("deck2", deck2);
            }
            tag.putInt("energy1", container.energy1);
            tag.putInt("energy2", container.energy2);
            tag.putInt("health1", container.health1);
            tag.putInt("health2", container.health2);
            tag.putInt("turn", container.turn);
            tag.putInt("turnEnergy", container.turnEnergy);
            ListTag gameState = new ListTag();
            for (int i = 0; i < 6; i++) {
                CompoundTag stateTag = new CompoundTag();
                container.game.state[i].save(stateTag);
                gameState.add(stateTag);
            }
            tag.put("gameState", gameState);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("name"))
            this.name = Component.Serializer.fromJson(tag.getString("name"));
        inGame = tag.getBoolean("game");
        p1 = tag.getBoolean("p1");
        if(p1) {
            if(container == null)
                container = new BattleContainer();
            CompoundTag inv = tag.getCompound("inv");
            for (int i = 0; i < 14; i++) {
                if(inv.contains("" + i))
                    container.setItem(i, ItemStack.of(inv.getCompound("" + i)));
            }
            if(tag.contains("deck1")) {
                CompoundTag deck1 = tag.getCompound("deck1");
                container.deck1 = new DeckboxContainer(container.getItem(0));
                for (int i = 0; i < 14; i++) {
                    if (deck1.contains("" + i))
                        container.deck1.setItem(i, ItemStack.of(deck1.getCompound("" + i)));
                }
            }
            if(tag.contains("deck2")) {
                CompoundTag deck2 = tag.getCompound("deck2");
                container.deck2 = new DeckboxContainer(container.getItem(7));
                for (int i = 0; i < 14; i++) {
                    if (deck2.contains("" + i))
                        container.deck2.setItem(i, ItemStack.of(deck2.getCompound("" + i)));
                }
            }
            container.reload();
            container.entity = this;
            if (container.battleLog.isEmpty() && tag.contains("log")) {
                ListTag log = tag.getList("log", Tag.TAG_COMPOUND);
                BattleComponent.LIST_CODEC.decode(NbtOps.INSTANCE, log).result().ifPresent(result -> container.battleLog.addAll(result.getFirst()));
            }
            container.energy1 = tag.getInt("energy1");
            container.energy2 = tag.getInt("energy2");
            container.health1 = tag.getInt("health1");
            container.health2 = tag.getInt("health2");
            container.turn = tag.getInt("turn");
            container.turnEnergy = tag.getInt("turnEnergy");
            ListTag gameState = tag.getList("gameState", Tag.TAG_COMPOUND);
            for (int i = 0; i < 6; i++) {
                container.game.state[i] = BattleCardState.load(gameState.getCompound(i));
            }
        }
        if (inGame && level != null && level.getBlockState(getBlockPos()).getBlock() instanceof PlaymatBlock && level.getBlockEntity(getBlockPos().relative(level.getBlockState(getBlockPos()).getValue(PlaymatBlock.DIR))) instanceof PlaymatBlockEntity opponent) {
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
        ItemStack removedDeck = container.getItem(p1 ? 0 : 7);
        container.setItem(p1 ? 0 : 7, itemInHand);
        name = itemInHand.getHoverName();
        setChanged();
        return removedDeck;
    }
}
