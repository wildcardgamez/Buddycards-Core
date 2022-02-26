package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuddycardItem extends Item {
    public BuddycardItem(BuddycardsItems.BuddycardRequirement shouldLoad, BuddycardSet set, int cardNumber, Rarity rarity, Item.Properties properties) {
        super(properties);
        SET = set;
        CARD_NUMBER = cardNumber;
        RARITY = rarity;
        REQUIREMENT = shouldLoad;

        BuddycardsAPI.registerCard(this);
    }

    protected final BuddycardSet SET;
    protected final int CARD_NUMBER;
    protected final Rarity RARITY;
    protected final BuddycardsItems.BuddycardRequirement REQUIREMENT;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        //Show the cards joke/tooltip
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
        //Show the set, card number, and shiny symbol if applicable
        TranslatableComponent cn = new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.number_separator");
        cn.append("" + CARD_NUMBER);
        if(isFoil(stack))
            cn.append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.foil_symbol"));
        tooltip.add(new TranslatableComponent(SET.getDescriptionId()).append(cn).withStyle(ChatFormatting.GRAY));
        if(stack.hasTag() && stack.getTag().contains("grade")) {
            tooltip.add(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.grade." + stack.getTag().getInt("grade")).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return getRarity();
    }

    public Rarity getRarity() {
        return RARITY;
    }

    public BuddycardSet getSet() {
        return SET;
    }

    public int getCardNumber() {
        return CARD_NUMBER;
    }

    public static void setShiny(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putBoolean("foil", true);
        stack.setTag(nbt);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        //Make shiny cards have enchant glow
        return stack.hasTag() && stack.getTag().contains("foil") && stack.getTag().getBoolean("foil");
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        //Only show cards in the creative menu when the respective mod is loaded
        if(this.allowdedIn(group) && REQUIREMENT.shouldLoad()) {
            ItemStack foil = new ItemStack(this);
            CompoundTag nbt = new CompoundTag();
            nbt.putBoolean("foil", true);
            foil.setTag(nbt);
            items.add(new ItemStack(this));
            items.add(foil);
        }
    }

    public boolean shouldLoad() {
        return REQUIREMENT.shouldLoad();
    }
}
