package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.integration.CuriosIntegration;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BuddysteelSetMedalItem extends MedalItem {
    public BuddysteelSetMedalItem(BuddycardsItems.BuddycardRequirement shouldLoad, IMedalTypes type, BuddycardSet set, Item.Properties properties) {
        super(type, properties);
        this.REQUIREMENT = shouldLoad;
        this.SET = set;
        this.SET.setMedal(() -> this);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if(level != null && stack.hasTag() && stack.getTag().contains("player") && stack.getTag().contains("day")) {
            tooltip.add(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddysteel_medal.name")
                    .append(stack.getTag().getString("player"))
                    .append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddysteel_medal.time"))
                    .append(String.valueOf(stack.getTag().getInt("day")))
                    .append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddysteel_medal.set"))
                    .append(new TranslatableComponent(SET.getDescriptionId())).withStyle(ChatFormatting.GRAY));
        }
        else
            tooltip.add(new TranslatableComponent(SET.getDescriptionId()).withStyle(ChatFormatting.GRAY));
    }

    protected final BuddycardsItems.BuddycardRequirement REQUIREMENT;
    protected final BuddycardSet SET;

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundTag unused) {
        return CuriosIntegration.initCapabilities(TYPE, stack);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if(REQUIREMENT.shouldLoad())
            super.fillItemCategory(group, items);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
