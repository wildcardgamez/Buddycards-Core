package com.wildcard.buddycards.util;

import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DamageEffectHandler {
    @SubscribeEvent
    public void onDamagePlayer (LivingDamageEvent event) {
        if (event.getEntity() instanceof Player entity) {
            if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem().equals(BuddycardsItems.LUMINIS_HELMET.get()) && !entity.getCooldowns().isOnCooldown(BuddycardsItems.LUMINIS_HELMET.get())) {
                if (entity.getHealth() <= event.getAmount()+2) {
                    event.setAmount(1);
                    entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), 2, Level.ExplosionInteraction.NONE);
                    entity.getCooldowns().addCooldown(BuddycardsItems.LUMINIS_HELMET.get(), 10000 / (1 + EnchantmentHelper.getItemEnchantmentLevel(BuddycardsMisc.RECOVERY.get(), entity.getItemBySlot(EquipmentSlot.HEAD))));
                    entity.getItemBySlot(EquipmentSlot.FEET).hurtAndBreak(15, entity, (p) -> p.broadcastBreakEvent(EquipmentSlot.HEAD));
                }
            }
            if (entity.getItemBySlot(EquipmentSlot.FEET).getItem().equals(BuddycardsItems.ZYLEX_BOOTS.get()) && !entity.getCooldowns().isOnCooldown(BuddycardsItems.ZYLEX_BOOTS.get())) {
                if (entity.getHealth() <= event.getAmount()+2) {
                    event.setAmount(1);
                    while(true)
                        if (entity.randomTeleport(entity.getX() + entity.level().getRandom().nextFloat() * 20 - 10, entity.getY(), entity.getZ() + entity.level().getRandom().nextFloat() * 20 - 10, true)) break;
                    entity.getItemBySlot(EquipmentSlot.FEET).hurtAndBreak(15, entity, (p) -> p.broadcastBreakEvent(EquipmentSlot.FEET));
                    entity.getCooldowns().addCooldown(BuddycardsItems.ZYLEX_BOOTS.get(), 5000 / (1 + EnchantmentHelper.getItemEnchantmentLevel(BuddycardsMisc.RECOVERY.get(), entity.getItemBySlot(EquipmentSlot.FEET))));
                }
            }
        }
    }
}
