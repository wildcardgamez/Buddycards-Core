package com.wildcard.buddycards.item;

import com.wildcard.buddycards.block.LockableBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class BuddysteelKeyItem extends Item {
    public BuddysteelKeyItem() {
        super(new Properties());
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() instanceof Player player && context.getLevel().getBlockEntity(context.getClickedPos()) instanceof LockableBlockEntity entity) {
            switch (entity.tryLock(player)) {
                case FAIL -> player.displayClientMessage(context.getLevel().getBlockState(context.getClickedPos()).getBlock().getName().append(Component.translatable("item.buddycards.buddysteel_key.result.fail")), true);
                case UNLOCK -> player.displayClientMessage(context.getLevel().getBlockState(context.getClickedPos()).getBlock().getName().append(Component.translatable("item.buddycards.buddysteel_key.result.unlock")), true);
                case LOCK -> player.displayClientMessage(context.getLevel().getBlockState(context.getClickedPos()).getBlock().getName().append(Component.translatable("item.buddycards.buddysteel_key.result.lock")), true);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
