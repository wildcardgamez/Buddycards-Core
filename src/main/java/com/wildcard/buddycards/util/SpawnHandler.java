package com.wildcard.buddycards.util;

import com.wildcard.buddycards.entity.EnderlingEntity;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SpawnHandler {
    @SubscribeEvent
    public void onSpawn(LivingSpawnEvent event) {
        if (event instanceof LivingSpawnEvent.CheckSpawn && event.getEntity() instanceof EnderMan && ((LivingSpawnEvent.CheckSpawn) event).getSpawnReason() == MobSpawnType.NATURAL) {
            double rand = event.getWorld().getRandom().nextDouble();
            if ((event.getEntity().level.dimension().equals(Level.END) && rand < ConfigManager.enderlingChanceEnd.get()) ||
                    (event.getEntity().level.dimension().equals(Level.NETHER) && rand < ConfigManager.enderlingChanceNether.get()) ||
                    (event.getEntity().level.dimension().equals(Level.OVERWORLD) && rand < ConfigManager.enderlingChanceOverworld.get())) {
                EnderlingEntity entity = new EnderlingEntity(BuddycardsEntities.ENDERLING.get(), event.getEntity().level);
                entity.setPos(event.getX() + 1, event.getY(), event.getZ());
                event.getEntity().level.addFreshEntity(entity);
            }
        }
    }
}
