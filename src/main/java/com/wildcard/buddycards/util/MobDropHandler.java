package com.wildcard.buddycards.util;

import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;

public class MobDropHandler {
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        Level world = entity.getCommandSenderWorld();
        if (event.getSource().getEntity() instanceof Player) {
            Collection<ItemEntity> drops = event.getDrops();
            //If killed by player, certain mobs have a chance to drop certain packs, defined in the configs
            if (entity instanceof ZombifiedPiglin && entity.isBaby()) {
                if (event.getEntity().getRandom().nextFloat() < ConfigManager.zombiePiglinChance.get()) {
                    drops.add(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(BuddycardsItems.PACK_BASE.get(), 1)));
                }
            }
            else if (entity instanceof ZombieVillager && entity.isBaby()) {
                if (event.getEntity().getRandom().nextFloat() < ConfigManager.zombieVillagerChance.get()) {
                    drops.add(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(BuddycardsItems.PACK_BASE.get(), 1)));
                }
            }
            else if (entity instanceof Zombie && entity.isBaby()) {
                if (event.getEntity().getRandom().nextFloat() < ConfigManager.zombieChance.get()) {
                    drops.add(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(BuddycardsItems.PACK_BASE.get(), 1)));
                }
            }
            else if (entity instanceof Villager && entity.isBaby()) {
                if (event.getEntity().getRandom().nextFloat() < ConfigManager.villagerChance.get()) {
                    drops.add(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(BuddycardsItems.PACK_BASE.get(), 1)));
                }
            }
            else if (entity instanceof Piglin && entity.isBaby()) {
                if (event.getEntity().getRandom().nextFloat() < ConfigManager.piglinChance.get()) {
                    drops.add(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(BuddycardsItems.PACK_NETHER.get(), 1)));
                }
            }
            else if (entity instanceof Shulker) {
                if (event.getEntity().getRandom().nextFloat() < ConfigManager.shulkerChance.get()) {
                    drops.add(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(BuddycardsItems.PACK_END.get(), 1)));
                }
            }
            else if (entity instanceof EnderDragon) {
                if (event.getEntity().getRandom().nextFloat() < ConfigManager.dragonChance.get()) {
                    drops.add(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(BuddycardsItems.PACK_END.get(),
                            event.getEntity().getRandom().nextInt(ConfigManager.dragonMaxPacks.get()) + 1)));
                }
            }
            else if (entity instanceof WitherBoss) {
                if (event.getEntity().getRandom().nextFloat() < ConfigManager.witherChance.get()) {
                    drops.add(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(BuddycardsItems.PACK_NETHER.get(),
                            event.getEntity().getRandom().nextInt(ConfigManager.witherMaxPacks.get()) + 1)));
                }
            }
        }
    }
}
