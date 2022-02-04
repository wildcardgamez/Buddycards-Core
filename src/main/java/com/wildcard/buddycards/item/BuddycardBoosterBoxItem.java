package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BuddycardBoosterBoxItem extends BlockItem {
    public BuddycardBoosterBoxItem(Block block, String set, Properties properties) {
        super(block, properties);
        SET = set;
    }

    protected final String SET;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.set_" + SET).withStyle(ChatFormatting.GRAY));
    }
}
