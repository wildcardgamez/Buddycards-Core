package com.wildcard.buddycards.item;

import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.savedata.BuddycardCollectionSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class BuddycardPackItem extends Item {
    public BuddycardPackItem(BuddycardsItems.BuddycardRequirement shouldLoad, int amount, int foils, SimpleWeightedRandomList<Rarity> rarityWeights, Properties properties) {
        super(properties);
        REQUIREMENT = shouldLoad;
        CARD_AMT = amount;
        FOIL_AMT = foils;
        this.rarityWeights = rarityWeights;

        if (this.rarityWeights.isEmpty()) {
            throw new IllegalArgumentException("No rarity weights provided");
        }
    }

    protected final BuddycardsItems.BuddycardRequirement REQUIREMENT;
    protected final int CARD_AMT;
    protected final int FOIL_AMT;
    protected final SimpleWeightedRandomList<Rarity> rarityWeights;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            //Prematurely delete the pack item so the card items can go in the same slot
            player.getItemInHand(hand).shrink(1);
            //Roll each card and throw it into a list
            NonNullList<ItemStack> cards = NonNullList.create();
            for (int i = 0; i < CARD_AMT; i++) {
                ItemStack card = new ItemStack(rollCard(level.getRandom()));
                //If its one of the last ones that needs foil, make it foil
                if (i >= CARD_AMT - FOIL_AMT)
                    BuddycardItem.setShiny(card);
                cards.add(card);
            }
            //Give each card to the player and put their collection data in
            cards.forEach((card) -> {
                ItemHandlerHelper.giveItemToPlayer(player, card);
                BuddycardCollectionSaveData.get(serverLevel).addPlayerCardFound(player.getUUID(), ((BuddycardItem) card.getItem()).getSet(), ((BuddycardItem) card.getItem()).getCardNumber());
            });
        }
        //Return success every time because we shrink it ourselves instead of using consume
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        //Only show in the creative menu when the respective mod is loaded
        if (this.allowdedIn(group) && REQUIREMENT.shouldLoad()) {
            items.add(new ItemStack(this));
        }
    }

    public BuddycardItem rollCard(Random random) {
        Optional<Rarity> optional = rarityWeights.getRandomValue(random);
        return optional
                .map(this::getPossibleCards)
                .map(cards -> cards.get(random.nextInt(cards.size())))
                .orElseThrow(() -> new IllegalStateException("Cardpack " + getRegistryName() + " does not contain cards for rarity"));
    }

    public abstract List<BuddycardItem> getPossibleCards(Rarity rarity);
}
