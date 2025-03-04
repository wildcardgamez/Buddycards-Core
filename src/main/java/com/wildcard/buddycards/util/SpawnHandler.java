package com.wildcard.buddycards.util;

import com.wildcard.buddycards.entity.EnderlingEntity;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SpawnHandler {
    @SubscribeEvent
    public void onSpawn(MobSpawnEvent event) {
        if (event.getEntity() instanceof EnderMan enderMan && enderMan.getSpawnType() == MobSpawnType.NATURAL) {
            Level level = enderMan.level();
            double rand = level.getRandom().nextDouble();
            if ((level.dimension().equals(Level.END) && rand < ConfigManager.enderlingChanceEnd.get()) ||
                    (level.dimension().equals(Level.NETHER) && rand < ConfigManager.enderlingChanceNether.get()) ||
                    (level.dimension().equals(Level.OVERWORLD) && rand < ConfigManager.enderlingChanceOverworld.get())) {
                EnderlingEntity entity = new EnderlingEntity(BuddycardsEntities.ENDERLING.get(), level);
                entity.setPos(event.getX() + 1, event.getY(), event.getZ());
                level.addFreshEntity(entity);
            }
        }
    }
}
