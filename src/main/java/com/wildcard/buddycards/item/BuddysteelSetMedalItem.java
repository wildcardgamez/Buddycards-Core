package com.wildcard.buddycards.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.ICollectionTieredItem;
import com.wildcard.buddycards.gear.IMedalTypes;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemLore;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class BuddysteelSetMedalItem extends Item implements ICurioItem, ICollectionTieredItem {
    public BuddysteelSetMedalItem(IMedalTypes type, BuddycardSet set, Item.Properties properties) {
        super(properties);
        this.SET = set;
        this.SET.setMedal(() -> this);
        this.TYPE = type;
    }

    protected final IMedalTypes TYPE;
    protected final BuddycardSet SET;

    @Override
    public Component getName(ItemStack stack) {
        return getTier(stack) == 3 ? Component.translatable("item.buddycards.perfect_buddysteel_medal") : super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(getTierComponent(stack));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> map = LinkedHashMultimap.create();
        TYPE.applyAttributes(map, getTier(stack));
        return map;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        TYPE.effectTick(slotContext.entity(), getTier(stack));
    }

    public void initializeMedal(ItemStack stack, ServerLevel level) {
        stack.set(DataComponents.LORE, new ItemLore(List.of(Component.translatable(SET.getDescriptionId()).append(Component.translatable("item.buddycards.buddysteel_medal.completion.0")).append("" + (int)(level.getDayTime()/24000)).withStyle(ChatFormatting.GRAY))));
    }

    public void updateMedal(ItemStack medal, ItemStack scanner, ServerLevel level) {
        int current = medal.get(BuddycardsComponents.COLLECTION_TIER);
        NonNullList<Component> lore = NonNullList.create();
        lore.addAll(NonNullList.copyOf(medal.get(DataComponents.LORE).lines()));
        if (current == 0) {
            if (((BuddysteelScannerItem)scanner.getItem()).getCompletionPercentageForSet(scanner, 1, SET.getName()) >= 1) {
                lore.add(Component.translatable("item.buddycards.buddysteel_medal.completion.1").append("" + (int)(level.getDayTime()/24000)).withStyle(ChatFormatting.YELLOW));
                medal.set(DataComponents.LORE, new ItemLore(lore));
                medal.set(BuddycardsComponents.COLLECTION_TIER, 1);
                updateMedal(medal, scanner, level);
            } else if (((BuddysteelScannerItem)scanner.getItem()).getCompletionPercentageForSet(scanner, 2, SET.getName()) >= 1) {
                lore.add(Component.translatable("item.buddycards.buddysteel_medal.completion.2").append("" + (int)(level.getDayTime()/24000)).withStyle(ChatFormatting.LIGHT_PURPLE));
                medal.set(DataComponents.LORE, new ItemLore(lore));
                medal.set(BuddycardsComponents.COLLECTION_TIER, 2);
                updateMedal(medal, scanner, level);
            }
        }
        else if (current == 1) {
            if (((BuddysteelScannerItem)scanner.getItem()).getCompletionPercentageForSet(scanner, 2, SET.getName()) >= 1) {
                lore.add(Component.translatable("item.buddycards.buddysteel_medal.completion.2").append("" + (int)(level.getDayTime()/24000)).withStyle(ChatFormatting.LIGHT_PURPLE));
                medal.set(DataComponents.LORE, new ItemLore(lore));
                medal.set(BuddycardsComponents.COLLECTION_TIER, 3);
                updateMedal(medal, scanner, level);
            }
        }
        else if (current == 2) {
            if (((BuddysteelScannerItem)scanner.getItem()).getCompletionPercentageForSet(scanner, 1, SET.getName()) >= 1) {
                lore.add(1, Component.translatable("item.buddycards.buddysteel_medal.completion.1").append("" + (int)(level.getDayTime()/24000)).withStyle(ChatFormatting.YELLOW));
                medal.set(DataComponents.LORE, new ItemLore(lore));
                medal.set(BuddycardsComponents.COLLECTION_TIER, 3);
                updateMedal(medal, scanner, level);
            }
        } else if (((BuddysteelScannerItem)scanner.getItem()).getCompletionPercentageForSet(scanner, 4, SET.getName()) >= 1) {
            lore.add(Component.translatable("item.buddycards.buddysteel_medal.completion.4").append("" + (int)(level.getDayTime()/24000)).withStyle(ChatFormatting.AQUA));
            medal.set(DataComponents.LORE, new ItemLore(lore));
            medal.set(BuddycardsComponents.COLLECTION_TIER, 4);
        }
    }

    public BuddycardSet getSet() {
        return SET;
    }
}
