package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.core.CardInfo;
import com.wildcard.buddycards.core.CardInfoProviderItem;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.stream.Stream;

public class BuddycardItem extends Item implements CardInfoProviderItem {
    public BuddycardItem(BuddycardsItems.BuddycardRequirement shouldLoad, BuddycardSet set, int cardNumber, Rarity rarity) {
        super(new Properties().rarity(rarity).component(BuddycardsComponents.BUDDYCARD_FOIL, 0).component(BuddycardsComponents.BUDDYCARD_GRADE, 0));
        SET = set;
        CARD_NUMBER = cardNumber;
        REQUIREMENT = shouldLoad;
        BuddycardsAPI.registerCard(this);
    }

    protected final BuddycardSet SET;
    protected final int CARD_NUMBER;
    protected final BuddycardsItems.BuddycardRequirement REQUIREMENT;

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        //Show the cards joke/tooltip
        tooltip.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.ITALIC));
        //Show the set, card number, and shiny symbol if applicable
        MutableComponent cn = Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.number_separator");
        cn.append("" + CARD_NUMBER);
        int foil = getFoil(stack);
        if(getFoil(stack) != 0) {
            if (foil == 1)
                cn.append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.foil_symbol").withStyle(ChatFormatting.YELLOW));
            else if (foil == 2)
                cn.append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.foil_symbol").withStyle(ChatFormatting.GOLD));
            else if (foil == 3)
                cn.append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.foil_symbol").withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        tooltip.add(Component.translatable(SET.getDescriptionId()).append(cn).withStyle(ChatFormatting.GRAY));
        //Show grade
        if(isGraded(stack)) {
            MutableComponent grade = Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.grade").withStyle(ChatFormatting.LIGHT_PURPLE);
            if (getGrade(stack) == 5)
                grade.append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.grade.5").withStyle(ChatFormatting.YELLOW));
            else
                grade.append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.grade." + getGrade(stack)).withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(grade);
        }
    }

    public BuddycardSet getSet() {
        return SET;
    }

    public int getCardNumber() {
        return CARD_NUMBER;
    }

    public boolean shouldLoad() {
        return REQUIREMENT.shouldLoad();
    }

    public Rarity getRarity() {
        return components().get(DataComponents.RARITY);
    }

    @Override
    public DataComponentMap components() {
        return super.components();
    }

    public static void setShiny(ItemStack stack, int type) {
        stack.set(BuddycardsComponents.BUDDYCARD_FOIL, type);
    }

    public static int getFoil(ItemStack stack) {
        var foil = stack.get(BuddycardsComponents.BUDDYCARD_FOIL);
        if (foil == null)
            return 0;
        return foil;
    }

    public static void setGrade(ItemStack stack, int grade) {
        stack.set(BuddycardsComponents.BUDDYCARD_GRADE, grade);
    }

    public boolean isGraded(ItemStack stack) {
        return getGrade(stack) != 0;
    }

    public static int getGrade(ItemStack stack) {
        var grade = stack.get(BuddycardsComponents.BUDDYCARD_GRADE);
        if (grade == null)
            return 0;
        return grade;
    }

    public static CardInfo getCardInfo(ItemStack stack) {
        BuddycardItem item = (BuddycardItem) stack.getItem();
        return new CardInfo(item.getSet().getName(), item.getCardNumber(), getFoil(stack), getGrade(stack));
    }

    public Stream<CardInfo> getAllCardInfo(ItemStack stack) {
        return Stream.of(new CardInfo(SET.getName(), CARD_NUMBER, getFoil(stack), getGrade(stack)));
    }
}
