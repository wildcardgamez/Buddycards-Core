package com.wildcard.buddycards.menu;

import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.core.CardInfo;
import com.wildcard.buddycards.item.BuddysteelScannerItem;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.List;
import java.util.stream.Stream;

public class ScannerMenu extends AbstractContainerMenu {
    private List<CardInfo> collectionInfo;
    private int currentSetId;
    private BuddycardSet currentSet;
    private ItemStackHandler fakeItems = new ItemStackHandler(82);
    private NonNullList<Float> percentages = NonNullList.withSize(81, 0f);
    private boolean doFoils, doGrades, doGradedFoils;
    private final DataSlot setData = DataSlot.standalone();

    public ScannerMenu(int id, Inventory playerInv) {
        this(id, ((BuddysteelScannerItem)playerInv.getSelected().getItem()).getAllCardInfo(playerInv.getSelected(), playerInv.player), playerInv.getSelected().get(BuddycardsComponents.COLLECTION_TIER));
    }

    public ScannerMenu(int id, Stream<CardInfo> info, int type) {
        super(BuddycardsMisc.SCANNER_MENU.get(), id);
        collectionInfo = info.toList();
        currentSetId = 0;
        if (type > 0 && type != 2)
            doFoils = true;
        if (type > 1)
            doGrades = true;
        if (type > 3)
            doGradedFoils = true;
        this.addDataSlot(setData);
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new ScannerSlot(fakeItems, x + (y * 9), 9 + x * 18, 18 + y * 18));
            }
        }
        this.addSlot(new ScannerSlot(fakeItems, 81, 145, -4));
        refreshSet();
    }

    class ScannerSlot extends SlotItemHandler {
        public ScannerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return false;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean isHighlightable() {
            return false;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public void refreshSet() {
        currentSet = BuddycardsAPI.getSetById(currentSetId);
        for (int i = 0; i < 81; i++) {
            int finalI = i + 1;
            //If its both within the set length and has a card in the collection, we populate it with a fake card and set the percentage
            if (i < currentSet.getCards().size() && collectionInfo.stream().anyMatch(card -> card.set().equals(currentSet.getName()) && card.number() == finalI)) {
                slots.get(i).set(new ItemStack(currentSet.getCardById(i)));
                if (doGradedFoils)
                    percentages.set(i, collectionInfo.stream().filter(card -> card.set().equals(currentSet.getName()) && card.number() == finalI).toList().size() / 24f);
            }
            //Otherwise, its empty
            else {
                slots.get(i).set(ItemStack.EMPTY);
                if (doGradedFoils)
                    percentages.set(i, 0f);
            }
        }
    }

    public BuddycardSet getCurrentSet() {
        if (currentSetId != setData.get()) {
            currentSetId = setData.get();
            refreshSet();
        }
        return currentSet;
    }

    public List<Component> getCollectionTooltip(int number) {
        //If it's the set info being hovered, give that tooltip
        if (number == 82)
            return List.of(Component.translatable(currentSet.getDescriptionId()), Component.translatable(currentSet.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
        //If it's bigger than the set, its blank
        if (number > currentSet.getCards().size())
            return null;
        List<CardInfo> cardInfo = collectionInfo.stream().filter(card -> card.set().equals(currentSet.getName()) && card.number() == number).toList();
        //If it's a missing card, give the missing card tooltip
        if (cardInfo.isEmpty())
            return List.of(Component.translatable("item.buddycards.buddysteel_scanner.blank"), Component.translatable("item.buddycards.buddysteel_scanner.blank.desc").withStyle(ChatFormatting.ITALIC), Component.literal("#" + number).withStyle(ChatFormatting.GRAY));
        //Otherwise, we get to making the checklist
        NonNullList<Component> tooltip = NonNullList.create();
        tooltip.add(Component.translatable(currentSet.getCardById(number - 1).getDescriptionId()));
        tooltip.add(Component.translatable(currentSet.getCardById(number - 1).getDescriptionId()+".desc").withStyle(ChatFormatting.ITALIC));
        tooltip.add(Component.literal("#" + number).withStyle(ChatFormatting.GRAY));
        //If its checking just foils, we do a simple foil checklist
        if (doFoils && !doGradedFoils){
            MutableComponent nongradedFoils = Component.empty();
            nongradedFoils.append(Component.literal(cardInfo.stream().anyMatch(card -> card.foil() == 0) ? "☑" : "☐").withStyle(ChatFormatting.GRAY));
            nongradedFoils.append(Component.literal(cardInfo.stream().anyMatch(card -> card.foil() == 1) ? "☑" : "☐").withStyle(ChatFormatting.YELLOW));
            nongradedFoils.append(Component.literal(cardInfo.stream().anyMatch(card -> card.foil() == 2) ? "☑" : "☐").withStyle(ChatFormatting.GOLD));
            nongradedFoils.append(Component.literal(cardInfo.stream().anyMatch(card -> card.foil() == 3) ? "☑" : "☐").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(nongradedFoils);
        }
        //If it's checking for grades, we put the column markers in, and fill it based on whether we care about them being foil or not
        if(doGrades) {
            tooltip.add(Component.translatable("item.buddycards.buddysteel_scanner.grades").withStyle(ChatFormatting.GRAY));
            MutableComponent nonfoilGrades = Component.empty();
            if (doGradedFoils) {
                for (int i = 0; i < 6; i++) {
                    int finalI = i;
                    nonfoilGrades.append(Component.literal(cardInfo.stream().anyMatch(card -> card.foil() == 0 && card.grade() == finalI) ? "☑" : "☐").withStyle(ChatFormatting.GRAY));
                }
            }
            else {
                for (int i = 0; i < 6; i++) {
                    int finalI = i;
                    nonfoilGrades.append(Component.literal(cardInfo.stream().anyMatch(card -> card.grade() == finalI) ? "☑" : "☐").withStyle(ChatFormatting.GRAY));
                }
            }
            tooltip.add(nonfoilGrades);
        }
        //If we are doing graded foils, they each get their own rows for checks
        if (doGradedFoils) {
                MutableComponent foils = Component.empty();
                for (int i = 0; i < 6; i++) {
                    int finalI = i;
                    foils.append(Component.literal(cardInfo.stream().anyMatch(card -> card.foil() == 1 && card.grade() == finalI) ? "☑" : "☐").withStyle(ChatFormatting.YELLOW));
                }
                tooltip.add(foils);
                MutableComponent lFoils = Component.empty();
                for (int i = 0; i < 6; i++) {
                    int finalI = i;
                    lFoils.append(Component.literal(cardInfo.stream().anyMatch(card -> card.foil() == 2 && card.grade() == finalI) ? "☑" : "☐").withStyle(ChatFormatting.GOLD));
                }
                tooltip.add(lFoils);
                MutableComponent rFoils = Component.empty();
                for (int i = 0; i < 6; i++) {
                    int finalI = i;
                    rFoils.append(Component.literal(cardInfo.stream().anyMatch(card -> card.foil() == 3 && card.grade() == finalI) ? "☑" : "☐").withStyle(ChatFormatting.LIGHT_PURPLE));
                }
                tooltip.add(rFoils);
        }
        return tooltip;
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (buttonId == 0) {
            currentSetId = currentSetId > 0 ? currentSetId - 1 : BuddycardsAPI.getAllSets().size() - 1;
            setData.set(currentSetId);
            refreshSet();
            broadcastChanges();
            return true;
        }
        else if (buttonId == 1) {
            currentSetId = currentSetId + 1 < BuddycardsAPI.getAllSets().size() ? currentSetId + 1 : 0;
            setData.set(currentSetId);
            refreshSet();
            broadcastChanges();
            return true;
        }
        return false;
    }


    public float getCompletionForCard(int number) {
        return percentages.get(number);
    }

    public boolean hasPercentages() {
        return doGradedFoils;
    }
}
