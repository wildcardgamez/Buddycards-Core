package com.wildcard.buddycards.item;

import com.wildcard.buddycards.container.DeckboxContainer;
import com.wildcard.buddycards.inventory.BinderInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;
import java.util.stream.Stream;

public class DeckboxItem extends Item {
    public DeckboxItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack deck = player.getItemInHand(hand);
        if(level instanceof ServerLevel) {
            //Find the amount of slots and then open the binder GUI
            NetworkHooks.openGui((ServerPlayer) player, new SimpleMenuProvider(
                    (id, playerInventory, entity) -> new DeckboxContainer(id, player.getInventory(), new BinderInventory(18, deck))
                    , player.getItemInHand(hand).getHoverName()));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    private static Stream<ItemStack> getContents(ItemStack p_150783_) {
        CompoundTag compoundtag = p_150783_.getTag();
        if (compoundtag == null) {
            return Stream.empty();
        } else {
            ListTag listtag = compoundtag.getList("Items", 10);
            return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        getContents(stack).forEach(nonnulllist::add);
        return Optional.of(new BundleTooltip(nonnulllist, nonnulllist.size()));
    }
}
