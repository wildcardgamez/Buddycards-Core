package com.wildcard.buddycards.item;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.battles.game.BattleAbility;
import com.wildcard.buddycards.battles.game.BattleEvent;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BuddycardItem extends Item {
    public BuddycardItem(BuddycardsItems.BuddycardRequirement shouldLoad, BuddycardSet set, int cardNumber, Rarity rarity, Properties properties, int cost, int power, ListMultimap<BattleEvent, BattleAbility> abilities) {
        super(properties);
        SET = set;
        CARD_NUMBER = cardNumber;
        RARITY = rarity;
        REQUIREMENT = shouldLoad;
        COST = cost;
        POWER = power;
        ABILITIES = ImmutableListMultimap.copyOf(abilities);

        BuddycardsAPI.registerCard(this);
    }

    protected final BuddycardSet SET;
    protected final int CARD_NUMBER;
    protected final Rarity RARITY;
    protected final BuddycardsItems.BuddycardRequirement REQUIREMENT;

    protected final int COST;
    protected final int POWER;
    protected final ImmutableListMultimap<BattleEvent, BattleAbility> ABILITIES;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TextComponent("" + COST).append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.cost"))
                .append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.number_separator"))
                .append("" + POWER).append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.power")));
        for (BattleAbility ability: ABILITIES.values()) {
            tooltip.add(new TranslatableComponent("battles.ability." + Buddycards.MOD_ID + "." + ability.name).withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent("battles.ability." + Buddycards.MOD_ID + "." + ability.name + ".desc").withStyle(ChatFormatting.DARK_GRAY));
        }
        //Show the cards joke/tooltip
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        //Show the set, card number, and shiny symbol if applicable
        TranslatableComponent cn = new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.number_separator");
        cn.append("" + CARD_NUMBER);
        if(isFoil(stack))
            cn.append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.foil_symbol"));
        tooltip.add(new TranslatableComponent(SET.getDescriptionId()).append(cn).withStyle(ChatFormatting.GRAY));
        //Show grade
        if(stack.hasTag() && stack.getTag().contains("grade")) {
            tooltip.add(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.grade." + stack.getTag().getInt("grade")).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        //Show battle stats
        if(stack.hasTag() && stack.getTag().contains("wins")) {
            tooltip.add(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.stats")
                    .append("" + stack.getTag().getInt("wins"))
                    .append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.power"))
                    .append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.number_separator"))
                    .append("" + stack.getTag().getInt("loss"))
                    .append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.skull"))
                    .withStyle(ChatFormatting.BLUE));
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
        return hasFoil(stack);
    }

    public static boolean hasFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("foil") && stack.getTag().getBoolean("foil");
    }

    public static int getGrade(ItemStack stack) {
        if(stack.hasTag() && stack.getTag().contains("grade"))
            return stack.getTag().getInt("grade");
        return 0;
    }
    
    public int getCost(ItemStack stack) {
        return this.COST;
    }
    
    public int getPower(ItemStack stack) {
        return this.POWER;
    }
    
    public ListMultimap<BattleEvent, BattleAbility> getAbilities() {
        return this.ABILITIES;
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

    public void setupOnBattlefield(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putInt("power", POWER);
        stack.setTag(nbt);
    }
}
