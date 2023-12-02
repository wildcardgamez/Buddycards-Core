package com.wildcard.buddycards.menu;

import com.wildcard.buddycards.battles.*;
import com.wildcard.buddycards.battles.game.BattleEvent;
import com.wildcard.buddycards.battles.game.BattleGame;
import com.wildcard.buddycards.block.PlaymatBlock;
import com.wildcard.buddycards.block.entity.PlaymatBlockEntity;
import com.wildcard.buddycards.container.BattleContainer;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsMisc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlaymatMenu extends AbstractContainerMenu {
    public PlaymatBlockEntity entity;
    public BattleContainer container;
    final boolean p1;
    public final DataSlot energy = DataSlot.standalone();
    public final DataSlot health = DataSlot.standalone();
    public final DataSlot opponentEnergy = DataSlot.standalone();
    public final DataSlot opponentHealth = DataSlot.standalone();
    public final DataSlot selectedSlot = DataSlot.standalone();
    private final Inventory inventory;
    private final List<SyncedData> syncedData = new ArrayList<>();

    public PlaymatMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, decodeBlockEntity(playerInv.player.level, buf.readBlockPos()));
    }

    private static PlaymatBlockEntity decodeBlockEntity(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof PlaymatBlockEntity entity)
            return entity;
        throw new IllegalStateException("Block entity expected to be PlaymatBlockEntity!" + pos);
    }

    public PlaymatMenu(int id, Inventory playerInv, PlaymatBlockEntity entity) {
        super(BuddycardsMisc.PLAYMAT_CONTAINER.get(), id);
        this.entity = entity;
        this.container = entity.container;
        this.p1 = entity.p1;
        this.addSlot(new DeckSlot(this.container, (p1 ? 7 : 0), 143, 18));
        this.addSlot(new DeckSlot(this.container, (p1 ? 0 : 7), 143, 64));
        Direction dir = entity.getBlockState().getValue(PlaymatBlock.DIR);
        for (int i = 0; i < 3; i++) {
            this.addSlot(new HandSlot(this, (p1 ? 1 : 8) + i, 80 + (18 * i), 64));
        }
        for (int i = 0; i < 3; i++) {
            this.addSlot(new BattlefieldSlot(this, (p1 ? 4 : 11)+i, (dir == Direction.SOUTH || dir == Direction.WEST) ? 80 + (18 * i) : 116 - (18 * i), 36));
        }
        for (int i = 0; i < 3; i++) {
            this.addSlot(new OpponentBattlefieldSlot(this, (p1 ? 11 : 4)+i, (dir == Direction.SOUTH || dir == Direction.WEST) ? 80 + (18 * i) : 116 - (18 * i), 18));
        }
        this.addDataSlot(energy);
        this.addDataSlot(health);
        this.addDataSlot(opponentEnergy);
        this.addDataSlot(opponentHealth);
        this.addDataSlot(selectedSlot);
        this.selectedSlot.set(SLOT_CLICKED_OUTSIDE);
        this.updateDataSlots();
        this.inventory = playerInv;
        syncedData.add(new SyncedData(
                () -> {
                    CompoundTag nbt = new CompoundTag();
                    Tag tag = BattleComponent.LIST_CODEC.encodeStart(NbtOps.INSTANCE, getBattleLog()).result().orElse(new ListTag());
                    nbt.put("data", tag);
                    return nbt;
                },
                nbt -> {
                    Tag data = nbt.get("data");

                    BattleComponent.LIST_CODEC.decode(NbtOps.INSTANCE, data).result().ifPresent(result -> {
                        container.battleLog.clear();
                        container.battleLog.addAll(result.getFirst());
                    });
                }
        ));
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    //click handling for card selection and playing
    @Override //no events or anything to hook into, just overriding this and calling the original
    public void clicked(int slotIndex, int button, ClickType clickType, Player player) {
        if (slotIndex == SLOT_CLICKED_OUTSIDE) {
            selectedSlot.set(SLOT_CLICKED_OUTSIDE);
        } else if (this.getSlot(slotIndex) instanceof CardSlot slot) {
            int slotNum = slot.getSlotIndex(); //confusing name, but this is slot.slot (container slot) while slotIndex is slot.index (menu slot) :/
            if (slot instanceof HandSlot) {
                //hand slots
                ItemStack stack = container.getItem(slotNum);
                if (stack.getItem() instanceof BuddycardItem item && container.game.canPlay(p1, item, stack)) {
                    selectedSlot.set(slotNum == selectedSlot.get() ? SLOT_CLICKED_OUTSIDE : slotNum);
                } else {
                    if (selectedSlot.get() != SLOT_CLICKED_OUTSIDE && stack.isEmpty()) {
                        ItemStack selectedItem = container.getItem(selectedSlot.get());
                        container.setItem(slotNum, selectedItem.split(1));
                    }
                    selectedSlot.set(SLOT_CLICKED_OUTSIDE);
                }
            } else if (slot instanceof BattlefieldSlot) {
                //battlefield slots
                int selSlot = selectedSlot.get();
                if (selSlot != SLOT_CLICKED_OUTSIDE && container.getItem(slotNum).isEmpty()) {
                    if (container.game.playCard(BattleGame.translateTo(slotNum), container.getItem(selSlot), p1)) {
                        container.setItem(selSlot, ItemStack.EMPTY);
                        container.game.trigger(BattleEvent.PLAYED, BattleGame.translateTo(slotNum));
                        this.broadcastChanges();
                    }
                    selectedSlot.set(SLOT_CLICKED_OUTSIDE);
                }
            }
        }
        super.clicked(slotIndex, button, clickType, player);
        setPlaymatsChanged();
    }

    public static final class ButtonIds {
        public static final int END_TURN = 1;
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (buttonId == ButtonIds.END_TURN && container.game.isP1() == p1) {
            container.game.endTurn();
            container.game.nextTurn();
            setPlaymatsChanged();
            return true;
        }
        
        return false; //return true if data slots are updated
    }

    private void setPlaymatsChanged() {
        entity.setChanged();
        BlockState blockState = entity.getBlockState();
        if (blockState.getBlock() instanceof PlaymatBlock) {
            if (entity.getLevel().getBlockEntity(entity.getBlockPos().relative(blockState.getValue(PlaymatBlock.DIR))) instanceof PlaymatBlockEntity opponent) {
                opponent.setChanged();
            }
        }
    }

    //Updates the values in the data slots. Does NOT synchronize them to clients!
    //clickMenuButton does it if you return true or you can call broadcastChanges();
    private void updateDataSlots() {
        if (this.p1) {
            this.energy.set(container.energy1);
            this.opponentEnergy.set(container.energy2);
            this.health.set(container.health1);
            this.opponentHealth.set(container.health2);
        } else {
            this.energy.set(container.energy2);
            this.opponentEnergy.set(container.energy1);
            this.health.set(container.health2);
            this.opponentHealth.set(container.health1);
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        sync(false);
    }

    @Override
    public void broadcastFullState() {
        super.broadcastFullState();
        sync(true);
    }

    private void sync(boolean fullSync) {
        if (inventory.player instanceof ServerPlayer player) {
            updateDataSlots();
            ListTag nbt = new ListTag();
            for (int i = 0; i < syncedData.size(); i++) {
                Optional<CompoundTag> optData = syncedData.get(i).getNBT(fullSync);
                if (optData.isPresent()) {
                    CompoundTag compoundTag = optData.get();
                    compoundTag.putInt("dataid", i);
                    nbt.add(compoundTag);
                }
            }
            if (!nbt.isEmpty()) {
                BuddycardsPackets.INSTANCE.send(PacketDistributor.PLAYER.with( () -> player), new BuddycardsSyncPacket(nbt));
            }
        }
    }

    public void consumeSync(ListTag nbt) {
        for (Tag tag : nbt) {
            if (tag instanceof CompoundTag cmpTag) {
                int dataid = cmpTag.getInt("dataid");
                syncedData.get(dataid).consumeNBT(cmpTag);
            }
        }
    }

    public abstract static class CardSlot extends Slot {
        
        public CardSlot(PlaymatMenu menu, int index, int xPosition, int yPosition) {
            super(menu.container, index, xPosition, yPosition);
        }

        //Only 1 card per slot
        @Override
        public int getMaxStackSize() {
            return 1;
        }
        
        //using custom movement system, disallow placing and pickup
        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
        
        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
    }
    
    public static class HandSlot extends CardSlot {
        public HandSlot(PlaymatMenu menu, int index, int xPosition, int yPosition) {
            super(menu, index, xPosition, yPosition);
        }
    }

    public static class BattlefieldSlot extends CardSlot {
        public BattlefieldSlot(PlaymatMenu menu, int index, int xPosition, int yPosition) {
            super(menu, index, xPosition, yPosition);
        }
    }

    public static class OpponentBattlefieldSlot extends CardSlot {
        public OpponentBattlefieldSlot(PlaymatMenu menu, int index, int xPosition, int yPosition) {
            super(menu, index, xPosition, yPosition);
        }
    }

    public static class DeckSlot extends Slot {
        public DeckSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //You cannot move the deck
        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
    }

    public static final class SyncedData {
        private final Supplier<CompoundTag> nbtGetter;
        private final Consumer<CompoundTag> nbtConsumer;
        private CompoundTag clientNBTPrediction = new CompoundTag();

        public SyncedData(Supplier<CompoundTag> nbtGetter, Consumer<CompoundTag> nbtConsumer) {
            this.nbtGetter = nbtGetter;
            this.nbtConsumer = nbtConsumer;
        }

        public Optional<CompoundTag> getNBT(boolean fullSync) {
            CompoundTag compoundTag = nbtGetter.get();
            if (fullSync || !compoundTag.equals(clientNBTPrediction)) {
                clientNBTPrediction = compoundTag;
                return Optional.of(compoundTag);
            }
            return Optional.empty();
        }

        public void consumeNBT(CompoundTag tag) {
            nbtConsumer.accept(tag);
        }
    }

    public List<BattleComponent> getBattleLog() {
        return container.battleLog;
    }
}
