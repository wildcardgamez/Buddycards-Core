package com.wildcard.buddycards.item;

import com.wildcard.buddycards.core.BuddycardSet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetBasedBlockItem extends BlockItem {
    public SetBasedBlockItem(Block block, Properties properties, BuddycardSet set) {
        super(block, properties);
        SET = set;
    }

    private final BuddycardSet SET;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(SET.getDescriptionId()).withStyle(ChatFormatting.GRAY));
    }
}
