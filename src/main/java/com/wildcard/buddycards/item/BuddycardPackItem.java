package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.block.BuddycardBoosterBoxBlock;
import com.wildcard.buddycards.registries.BuddycardsBlocks;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.savedata.BuddycardCollectionSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BuddycardPackItem extends Item {
    public BuddycardPackItem(BuddycardsItems.BuddycardRequirement shouldLoad, String set, int amount, int foils, Properties properties) {
        super(properties);
        SET = set;
        REQUIREMENT = shouldLoad;
        CARD_AMT = amount;
        FOIL_AMT = foils;
    }

    protected final String SET;
    protected final BuddycardsItems.BuddycardRequirement REQUIREMENT;
    protected final int CARD_AMT;
    protected final int FOIL_AMT;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.set_" + SET).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(level instanceof ServerLevel serverLevel) {
            //Prematurely delete the pack item so the card items can go in the same slot
            player.getItemInHand(hand).shrink(1);
            //Roll each card and throw it into a list
            NonNullList<ItemStack> cards = NonNullList.create();
            for (int i = 0; i < CARD_AMT; i++) {
                ItemStack card = new ItemStack(rollCard(level.getRandom()));
                //If its one of the last ones that needs foil, make it foil
                if(i >= CARD_AMT - FOIL_AMT)
                    BuddycardItem.setShiny(card);
                cards.add(card);
            }
            //Give each card to the player and put their collection data in
            cards.forEach((card) -> {
                ItemHandlerHelper.giveItemToPlayer(player, card);
                BuddycardCollectionSaveData.get(serverLevel).addPlayerCardFound(player.getUUID(), ((BuddycardItem)card.getItem()).getSet(), ((BuddycardItem)card.getItem()).getCardNumber());
            });
        }
        //Return success every time because we shrink it ourselves instead of using consume
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        //Only show in the creative menu when the respective mod is loaded
        if(this.allowdedIn(group) && REQUIREMENT.shouldLoad()) {
            items.add(new ItemStack(this));
        }
    }

    public BuddycardItem rollCard(Random random) {
        float rand = random.nextFloat();
        Rarity rarity;
        if (rand < .6)
            rarity = Rarity.COMMON;
        else if (rand < .9)
            rarity = Rarity.UNCOMMON;
        else if (rand < .975)
            rarity = Rarity.RARE;
        else
            rarity = Rarity.EPIC;
        ArrayList<BuddycardItem> set = BuddycardItem.CARD_LIST.get(SET);
        BuddycardItem card = set.get((int) (random.nextFloat() * set.size()));
        while (card.getRarity() != rarity) {
            card = set.get((int) (random.nextFloat() * set.size()));
        }
        return card;
    }
}
