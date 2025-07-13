package com.wildcard.buddycards.util;

import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SpawnHandler {
    @SubscribeEvent
    public void onSpawn(MobSpawnEvent.FinalizeSpawn event) {
        ServerLevel level = event.getLevel().getLevel();
        if (event.getEntity() instanceof EnderMan && event.getSpawnType() == MobSpawnType.NATURAL) {
            double rand = level.getRandom().nextDouble();
            if ((level.dimension().equals(Level.END) && rand < ConfigManager.enderlingChanceEnd.get()) ||
                    (level.dimension().equals(Level.NETHER) && rand < ConfigManager.enderlingChanceNether.get()) ||
                    (level.dimension().equals(Level.OVERWORLD) && rand < ConfigManager.enderlingChanceOverworld.get())) {
                BuddycardsEntities.ENDERLING.get().spawn(level, event.getEntity().blockPosition(), MobSpawnType.MOB_SUMMONED);
            }
        }
    }
}
