package com.wildcard.buddycards.menu;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.block.entity.PlaymatBlockEntity;
import com.wildcard.buddycards.container.BattleContainer;
import com.wildcard.buddycards.registries.BuddycardsMisc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import java.util.ArrayList;

public class PlaymatMenu extends AbstractContainerMenu {
    PlaymatBlockEntity entity;
    BattleContainer container;
    final boolean p1;
    public final DataSlot energy = DataSlot.standalone();
    public final DataSlot health = DataSlot.standalone();
    public final DataSlot opponentEnergy = DataSlot.standalone();
    public final DataSlot opponentHealth = DataSlot.standalone();

    public PlaymatMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, decodeBlockEntity(playerInv.player.level, buf.readBlockPos()));
    }

    private static PlaymatBlockEntity decodeBlockEntity(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof PlaymatBlockEntity entity) return entity;
        else throw new IllegalStateException("Block entity expected to be PlaymatBlockEntity!" + pos);
    }

    public PlaymatMenu(int id, Inventory playerInv, PlaymatBlockEntity entity) {
        super(BuddycardsMisc.PLAYMAT_CONTAINER.get(), id);
        this.entity = entity;
        this.container = entity.container;
        this.p1 = entity.p1;
        this.addSlot(new DeckSlot(this.container, (p1 ? 7 : 0), 143, 18));
        this.addSlot(new DeckSlot(this.container, (p1 ? 0 : 7), 143, 64));
        for (int i = 0; i < 3; i++) {
            this.addSlot(new CardSlot(this.container, (p1 ? 1 : 8) + i, 80 + (18 * i), 64));
        }
        for (int i = 0; i < 3; i++) {
            this.addSlot(new BattlefieldSlot(this.container, (p1 ? 4 : 11)+i, 80 + (18 * i), 36));
        }
        for (int i = 0; i < 3; i++) {
            this.addSlot(new OpponentBattlefieldSlot(this.container, (p1 ? 11 : 4)+i, 80 + (18 * i), 18));
        }
        this.addDataSlot(energy);
        this.addDataSlot(health);
        this.addDataSlot(opponentEnergy);
        this.addDataSlot(opponentHealth);
        this.updateDataSlots();
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
        // menu click logic soon...
        // if (buttonId == ButtonIds.END_TURN) or a switch or something
        
        //example for modifying health and synchronizing data
        //uncomment the next 4 lines to try it out
//        if (this.p1) container.health2--;
//        else container.health1--;
//        updateDataSlots();
//        return true;
        
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

    public static class CardSlot extends Slot {
        public CardSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Only 1 card per slot
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    public static class BattlefieldSlot extends CardSlot {
        public BattlefieldSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Once a card enters the battlefield, you cant move it
        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
    }

    public static class OpponentBattlefieldSlot extends BattlefieldSlot {
        public OpponentBattlefieldSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
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

    public MutableComponent getBattleLog() {
        ArrayList<MutableComponent> battleLog = container.battleLog;
        if(battleLog.isEmpty())
            return new TranslatableComponent(Buddycards.MOD_ID + ".broken");
        MutableComponent component = battleLog.get(battleLog.size() - 1);
        if (battleLog.size() > 1) for (int i = battleLog.size() - 2; i >= 0; i--) {
            component.append("\n").append(battleLog.get(i));
        }
        return component;
    }
}
