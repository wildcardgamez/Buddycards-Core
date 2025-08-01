package com.wildcard.buddycards.item;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.battles.game.BattleAbility;
import com.wildcard.buddycards.battles.game.BattleEvent;
import com.wildcard.buddycards.client.renderer.BuddycardsBlockEntityWithoutLevelRenderer;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.util.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

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
    
    @Deprecated
    public BuddycardItem(BuddycardsItems.BuddycardRequirement shouldLoad, BuddycardSet set, int cardNumber, Rarity rarity, Properties properties) {
        super(properties);
        SET = set;
        CARD_NUMBER = cardNumber;
        RARITY = rarity;
        REQUIREMENT = shouldLoad;
        COST = 2;
        POWER = 2;
        ABILITIES = ImmutableListMultimap.of();

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
        //Show cost, base power, and abilities for battles
        if(ConfigManager.enableBattles.get()) {
            if (ABILITIES.size() > 0) {
                tooltip.add(Component.literal("" + COST).append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.cost"))
                        .append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.number_separator"))
                        .append("" + POWER).append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.power")));
                for (BattleAbility ability : ABILITIES.values()) {
                    tooltip.add(Component.translatable("battles.ability." + Buddycards.MOD_ID + "." + ability.name).withStyle(ChatFormatting.GRAY));
                    tooltip.add(Component.translatable("battles.ability." + Buddycards.MOD_ID + "." + ability.name + ".desc").withStyle(ChatFormatting.DARK_GRAY));
                }
            } else
                tooltip.add(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.unimplemented").withStyle(ChatFormatting.DARK_GRAY));
        }
        //Show the cards joke/tooltip
        tooltip.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.ITALIC));
        //Show the set, card number, and shiny symbol if applicable
        MutableComponent cn = Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.number_separator");
        cn.append("" + CARD_NUMBER);
        if(isFoil(stack)) {
            cn.append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.foil_symbol." + getFoil(stack)));
        }
        tooltip.add(Component.translatable(SET.getDescriptionId()).append(cn).withStyle(ChatFormatting.GRAY));
        //Show grade
        if(isGraded(stack)) {
            tooltip.add(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.grade." + getGrade(stack)).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        //Show battle stats
        if(stack.hasTag() && stack.getTag().contains("wins")) {
            tooltip.add(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.stats")
                    .append("" + stack.getTag().getInt("wins"))
                    .append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.power"))
                    .append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.number_separator"))
                    .append("" + stack.getTag().getInt("loss"))
                    .append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.skull"))
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
        setShiny(stack, 1);
    }

    public static void setShiny(ItemStack stack, int type) {
        CompoundTag nbt = stack.getOrCreateTag().copy();
        nbt.putInt("foil", type);
        stack.setTag(nbt);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("foil") && stack.getTag().getInt("foil") != 0;
    }

    public static int getFoil(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("foil"))
            return stack.getTag().getInt("foil");
        return 0;
    }

    public boolean isGraded(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("grade") && stack.getTag().getInt("grade") != 0;
    }

    public static int getGrade(ItemStack stack) {
        if(stack.hasTag() && stack.getTag().contains("grade"))
            return stack.getTag().getInt("grade");
        return 0;
    }
    
    public int getCost() {
        return this.COST;
    }
    
    public int getPower() {
        return this.POWER;
    }
    
    public ListMultimap<BattleEvent, BattleAbility> getAbilities() {
        return this.ABILITIES;
    }

    public boolean shouldLoad() {
        return REQUIREMENT.shouldLoad();
    }

    public BuddycardItem getOriginal() {
        return this;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BuddycardsBlockEntityWithoutLevelRenderer.instance;
            }
        });
    }
}
