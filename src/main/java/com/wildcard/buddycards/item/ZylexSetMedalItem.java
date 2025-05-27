package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ZylexSetMedalItem extends BuddysteelSetMedalItem {
    public ZylexSetMedalItem(IMedalTypes type, BuddycardSet set, Properties properties) {
        super(type, set, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if(level != null && stack.hasTag() && stack.getTag().contains("player") && stack.getTag().contains("day")) {
            tooltip.add(Component.translatable("item." + Buddycards.MOD_ID + ".buddysteel_medal.name")
                    .append(stack.getTag().getString("player"))
                    .append(Component.translatable("item." + Buddycards.MOD_ID + ".buddysteel_medal.time"))
                    .append(String.valueOf(stack.getTag().getInt("day")))
                    .append(Component.translatable("item." + Buddycards.MOD_ID + ".zylex_medal.set"))
                    .append(Component.translatable(SET.getDescriptionId())).withStyle(ChatFormatting.GRAY));
        }
        else
            tooltip.add(Component.translatable(SET.getDescriptionId()).withStyle(ChatFormatting.GRAY));
    }
}
