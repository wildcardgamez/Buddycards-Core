package com.wildcard.buddycards.item;

import com.wildcard.buddycards.container.BinderContainer;
import com.wildcard.buddycards.savedata.EnderBinderSaveData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class EnderBinderItem extends Item {
    public EnderBinderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(player instanceof ServerPlayer) {
            //Open the GUI on server side
            NetworkHooks.openGui((ServerPlayer) player, new SimpleMenuProvider(
                    (id, playerInventory, entity) -> new BinderContainer(id, player.getInventory(),
                            EnderBinderSaveData.get(((ServerPlayer) player).getLevel()).getOrMakeEnderBinder(player.getUUID()))
                    , player.getItemInHand(hand).getHoverName()));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
