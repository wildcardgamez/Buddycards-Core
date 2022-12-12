package com.wildcard.buddycards.item;

import com.wildcard.buddycards.inventory.DeckboxInventory;
import com.wildcard.buddycards.menu.DeckboxMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class DeckboxItem extends Item {
    public DeckboxItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack deck = player.getItemInHand(hand);
        if (level instanceof ServerLevel) {
            NetworkHooks.openGui((ServerPlayer) player, new SimpleMenuProvider(
                    (id, playerInventory, entity) -> new DeckboxMenu(id, player.getInventory(), new DeckboxInventory(deck))
                    , player.getItemInHand(hand).getHoverName()));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        if (stack.hasTag() && stack.getTag().contains("Deck")) {
            CompoundTag deck = stack.getTag().getCompound("Deck");
            for (String i : deck.getAllKeys())
                tooltip.add(new TextComponent(deck.get(i) + " " + i));
        }
    }

    public static void updateFull(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if(tag.contains("Items") && tag.getList("Items", Tag.TAG_COMPOUND).size() == 16)
            tag.putInt("full", 1);
        else
            tag.putInt("full", 0);
        stack.setTag(tag);
    }
}
