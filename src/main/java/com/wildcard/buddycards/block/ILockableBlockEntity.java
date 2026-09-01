package com.wildcard.buddycards.block;

import net.minecraft.world.entity.player.Player;

public interface ILockableBlockEntity {
    abstract LockResult tryLock(Player player);

    public static enum LockResult {
        FAIL, LOCK, UNLOCK, ERROR;
    }
}
