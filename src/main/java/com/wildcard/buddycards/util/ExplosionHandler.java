package com.wildcard.buddycards.util;

import com.wildcard.buddycards.registries.BuddycardsBlocks;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ExplosionHandler {
    @SubscribeEvent
    public void onExplosion (ExplosionEvent.Detonate event) {
        int luminisBlocks = 0;
        List<BlockPos> replacedExplosion = new ArrayList<>();
        Level level = event.getLevel();
        for (int i = 0; i < event.getAffectedBlocks().size(); i++) {
            BlockPos blockPos = event.getAffectedBlocks().get(i);
            BlockState targetBlock = level.getBlockState(blockPos);
            //Check if the block is a kinetic chamber
            if (level instanceof ServerLevel server && targetBlock.getBlock().equals(BuddycardsBlocks.KINETIC_CHAMBER.get()))
                level.getBlockEntity(blockPos, BuddycardsEntities.KINETIC_CHAMBER_TILE.get()).ifPresent(be -> be.absorbExplosion(server));
            //Or luminis
            else if (targetBlock.getBlock().equals(BuddycardsBlocks.LUMINIS_BLOCK.get())) {
                luminisBlocks++;
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
                replacedExplosion.add(blockPos);
            }
            //Or another block
            else
                replacedExplosion.add(blockPos);
        }
        //Replace with
        event.getExplosion().clearToBlow();
        event.getAffectedBlocks().addAll(replacedExplosion);
        //Calculate rng, on average 1
        double rng = level.getRandom().nextFloat() * 2;
        //Calculate and give amt of crimson luminis
        int amt = (int) (luminisBlocks * rng / ConfigManager.luminisToCrimsonAvg.get());
        if (amt >= 1)
            level.addFreshEntity(new ItemEntity(level, event.getExplosion().getPosition().x, event.getExplosion().getPosition().y, event.getExplosion().getPosition().z,
                    new ItemStack(BuddycardsItems.CRIMSON_LUMINIS.get(), amt)));
    }
}
