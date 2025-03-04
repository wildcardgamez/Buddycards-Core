package com.wildcard.buddycards.item;

import com.wildcard.buddycards.menu.BinderMenu;
import com.wildcard.buddycards.savedata.EnderBinderSaveData;
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

public class EnderBinderItem extends Item {
    public EnderBinderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(player instanceof ServerPlayer serverPlayer && world instanceof ServerLevel serverLevel) {
            //Open the GUI on server side
            NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                    (id, playerInventory, entity) -> new BinderMenu(id, player.getInventory(),
                            EnderBinderSaveData.get(serverLevel).getOrMakeEnderBinder(player.getUUID()))
                    , player.getItemInHand(hand).getHoverName()));
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }
}
