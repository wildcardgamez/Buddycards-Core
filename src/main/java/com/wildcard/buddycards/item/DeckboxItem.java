package com.wildcard.buddycards.item;

import com.wildcard.buddycards.menu.DeckboxMenu;
import com.wildcard.buddycards.inventory.DeckboxInventory;
<<<<<<< Updated upstream
import net.minecraft.core.NonNullList;
=======
>>>>>>> Stashed changes
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
<<<<<<< Updated upstream
=======
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
>>>>>>> Stashed changes
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

<<<<<<< Updated upstream
import java.util.Optional;
import java.util.stream.Stream;
=======
import java.util.List;
>>>>>>> Stashed changes

public class DeckboxItem extends Item {
    public DeckboxItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack deck = player.getItemInHand(hand);
        if(level instanceof ServerLevel) {
            NetworkHooks.openGui((ServerPlayer) player, new SimpleMenuProvider(
                    (id, playerInventory, entity) -> new DeckboxMenu(id, player.getInventory(), new DeckboxInventory(deck))
                    , player.getItemInHand(hand).getHoverName()));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        if(stack.hasTag() && stack.getTag().contains("Items")) {
            for (Tag tag : stack.getTag().getList("Items", Tag.TAG_COMPOUND)) {
                if(tag instanceof CompoundTag nbt)
                    nonnulllist.add(nbt.getInt("Slot"), ItemStack.of(nbt));
            }
        }
        return Optional.of(new BundleTooltip(nonnulllist, nonnulllist.size()));
    }
}
