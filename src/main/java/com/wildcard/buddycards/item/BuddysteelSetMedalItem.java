package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.util.CuriosIntegration;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.HashMap;
import java.util.List;

public class BuddysteelSetMedalItem extends MedalItem {
    public static final HashMap<String, BuddysteelSetMedalItem> MEDALS = new HashMap<>();

    public BuddysteelSetMedalItem(BuddycardsItems.BuddycardRequirement shouldLoad, IMedalTypes type, String set, Item.Properties properties) {
        super(type, properties);
        this.REQUIREMENT = shouldLoad;
        this.SET = set;
        MEDALS.put(set, this);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if(level != null && stack.hasTag() && stack.getTag().contains("player") && stack.getTag().contains("day")) {
            tooltip.add(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddysteel_medal.name")
                    .append(stack.getTag().getString("player"))
                    .append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddysteel_medal.time"))
                    .append(String.valueOf(stack.getTag().getInt("day")))
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddysteel_medal.set")
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.set_" + SET)
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    protected final BuddycardsItems.BuddycardRequirement REQUIREMENT;
    protected final String SET;

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundTag unused) {
        return CuriosIntegration.initCapabilities(TYPE, stack);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if(REQUIREMENT.shouldLoad())
            super.fillItemCategory(group, items);
    }

    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    public int getEnchantmentValue() {
        return 1;
    }
}
