package com.wildcard.buddycards.menu;

import com.wildcard.buddycards.battles.*;
import com.wildcard.buddycards.block.entity.PlaymatBlockEntity;
import com.wildcard.buddycards.container.BattleContainer;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsMisc;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlaymatMenu extends AbstractContainerMenu {
    PlaymatBlockEntity entity;
    BattleContainer container;
    final boolean p1;
    public final DataSlot energy = DataSlot.standalone();
    public final DataSlot health = DataSlot.standalone();
    public final DataSlot opponentEnergy = DataSlot.standalone();
    public final DataSlot opponentHealth = DataSlot.standalone();
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
        for (int i = 0; i < 3; i++) {
            this.addSlot(new HandSlot(this, p1, (p1 ? 1 : 8) + i, 80 + (18 * i), 64));
        }
        for (int i = 0; i < 3; i++) {
            this.addSlot(new BattlefieldSlot(this, p1, (p1 ? 4 : 11)+i, 80 + (18 * i), 36));
        }
        for (int i = 0; i < 3; i++) {
            this.addSlot(new OpponentBattlefieldSlot(this, p1, (p1 ? 11 : 4)+i, 80 + (18 * i), 18));
        }
        this.addDataSlot(energy);
        this.addDataSlot(health);
        this.addDataSlot(opponentEnergy);
        this.addDataSlot(opponentHealth);
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

    public static final class ButtonIds {
        public static final int END_TURN = 1;
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (buttonId == ButtonIds.END_TURN && container.game.isP1() == p1) {
            container.game.endTurn();
            container.game.nextTurn();
            return true;
        }
        
        return false; //return true if data slots are updated
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

    public static abstract class CardSlot extends Slot {
        public final PlaymatMenu menu;
        public final BattleContainer battleContainer;
        public final boolean p1;
        
        public CardSlot(PlaymatMenu menu, boolean p1, int index, int xPosition, int yPosition) {
            super(menu.container, index, xPosition, yPosition);
            this.menu = menu;
            this.battleContainer = menu.container;
            this.p1 = p1;
        }

        //Only 1 card per slot
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
    
    public static class HandSlot extends CardSlot {
        public HandSlot(PlaymatMenu menu, boolean p1, int index, int xPosition, int yPosition) {
            super(menu, p1, index, xPosition, yPosition);
        }
        
        @Override
        public boolean mayPickup(Player player) {
            ItemStack stack = this.getItem();
            if (stack.getItem() instanceof BuddycardItem item) {
                return battleContainer.game.canPlay(p1, item, stack);
            } else {
                //what
                return false;
            }
        }
    }

    public static class BattlefieldSlot extends CardSlot {
        public BattlefieldSlot(PlaymatMenu menu, boolean p1, int index, int xPosition, int yPosition) {
            super(menu, p1, index, xPosition, yPosition);
        }

        //Once a card enters the battlefield, you cant move it
        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
        
        //crude detection
        @Override
        public void set(ItemStack stack) {
            battleContainer.game.playCard(battleContainer.game.translateTo(this.getSlotIndex()), stack, p1);
            super.set(stack);
            menu.broadcastChanges();
        }
    }

    public static class OpponentBattlefieldSlot extends BattlefieldSlot {
        public OpponentBattlefieldSlot(PlaymatMenu menu, boolean p1, int index, int xPosition, int yPosition) {
            super(menu, p1, index, xPosition, yPosition);
        }

        //You cannot play cards on the opponents battlefield
        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
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
