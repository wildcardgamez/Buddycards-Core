package com.wildcard.buddycards.util;

import com.wildcard.buddycards.registries.BuddycardsBlocks;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExplosionHandler {
    @SubscribeEvent
    public void explosion (ExplosionEvent.Detonate event) {
        int luminisBlocks = 0, crimsonLuminisBlocks = 0;
        for (int i = 0; i < event.getAffectedBlocks().size(); i++) {
            BlockPos blockPos = event.getAffectedBlocks().get(i);
            BlockState targetBlock = event.getWorld().getBlockState(blockPos);
            //Count the block if it's crimson luminis
            if (targetBlock.getBlock().equals(BuddycardsBlocks.CRIMSON_LUMINIS_BLOCK.get())) {
                crimsonLuminisBlocks++;
                event.getWorld().setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
            }
            //Or normal luminis
            else if (targetBlock.getBlock().equals(BuddycardsBlocks.LUMINIS_BLOCK.get())) {
                luminisBlocks++;
                event.getWorld().setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
            }
        }
        //Calculate rng, needs 2 blocks on average per crimson luminis
        double rng = event.getWorld().getRandom().nextFloat();
        if ((int) (luminisBlocks * rng) > 1)
            event.getWorld().addFreshEntity(new ItemEntity(event.getWorld(), event.getExplosion().getPosition().x, event.getExplosion().getPosition().y, event.getExplosion().getPosition().z,
                    new ItemStack(BuddycardsItems.CRIMSON_LUMINIS.get(), (int) (luminisBlocks * rng))));
    }
}
