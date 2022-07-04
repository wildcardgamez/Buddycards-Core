package com.wildcard.buddycards.entity;

import com.mojang.datafixers.util.Pair;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.event.entity.EntityTeleportEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnderlingEntity extends PathfinderMob implements Npc, Nameable {
    public static final Ingredient TEMPTATION_ITEMS = Ingredient.of(BuddycardsItems.ZYLEX_BLOCK.get(), BuddycardsItems.ZYLEX.get(), BuddycardsItems.VOID_ZYLEX_BLOCK.get(), BuddycardsItems.VOID_ZYLEX.get());

    ArrayList<Pair<ItemStack, ItemStack>> goalCards = new ArrayList<>();

    public EnderlingEntity(EntityType<? extends PathfinderMob> type, Level lvl) {
        super(type, lvl);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    public static AttributeSupplier.Builder setupAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, .5D)
                .add(Attributes.FOLLOW_RANGE, 6.0f);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1f));
        this.goalSelector.addGoal(2, new TemptGoal(this, .75f, TEMPTATION_ITEMS, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, .5f, 0.0f));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 8.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FollowMobGoal(this, 0.6f, 1.5f, 6.0f));
    }

    @Override
    protected int getExperienceReward(Player player) {
        return 1 + this.level.random.nextInt(3);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMAN_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENDERMAN_HURT;
    }

    protected void customServerAiStep() {
        if (this.level.isDay() && this.tickCount >= 600) {
            float f = this.getBrightness();
            if (f > 0.5F && this.level.canSeeSky(this.blockPosition()) && this.random.nextFloat() * 60.0F < (f - 0.4F) * 2.0F) {
                this.teleport();
            }
        }
        super.customServerAiStep();
    }

    protected boolean teleport() {
        if (!this.level.isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 32.0D;
            double d1 = this.getY() + (double)(this.random.nextInt(32) - 32);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 32.0D;
            return this.teleport(d0, d1, d2);
        } else {
            return false;
        }
    }

    private boolean teleport(double p_70825_1_, double p_70825_3_, double p_70825_5_) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(p_70825_1_, p_70825_3_, p_70825_5_);
        while(blockpos$mutable.getY() > 0 && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
            blockpos$mutable.move(Direction.DOWN);
        }
        BlockState blockstate = this.level.getBlockState(blockpos$mutable);
        if (blockstate.getMaterial().blocksMotion() && !blockstate.getFluidState().is(Fluids.WATER)) {
            EntityTeleportEvent event = new EntityTeleportEvent(this, p_70825_1_, p_70825_3_, p_70825_5_);
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
            boolean success = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (success && !this.isSilent()) {
                this.level.playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
            return success;
        } else {
            return false;
        }
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            for(int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        }
        super.aiStep();
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    public boolean isBaby() {
        return true;
    }

    public void setupGoalCards (Random rand) {
        List<BuddycardItem> cards = BuddycardsAPI.getAllCards().stream().filter(BuddycardItem::shouldLoad).toList();
        for (int i = 0; i < 6; i++) {
            ItemStack card = new ItemStack(cards.get(rand.nextInt(cards.size())));
            if (i % 3 == 0)
                BuddycardItem.setShiny(card);
            goalCards.add(new Pair<>(card, getCardSellValue(card, rand)));
            if (i < 3) {
                ItemStack card2 = card.copy();
                card2.getOrCreateTag().putInt("grade", rand.nextInt(1,5));
                goalCards.add(new Pair<>(card2, getCardSellValue(card2, rand)));
            }
        }
    }

    public ItemStack getCardSellValue(ItemStack card, Random rand) {
        int value = rand.nextInt(2, 4);
        boolean markVoid = false;
        if (card.getRarity() == Rarity.EPIC)
            value += 6;
        if (card.hasFoil())
            value += 3;
        if(card.hasTag() && card.getTag().contains("grade")) {
            int grade = card.getTag().getInt("grade");
            if(grade == 1 || grade == 2)
                value -= 2;
            else if(grade == 3)
                value *= 2;
            else if (grade == 4)
                value *= 4;
            else if (grade == 5)
                value *= 18;
            if(grade >= 3 || card.getRarity() == Rarity.EPIC)
                markVoid = true;
        }
        if (value == 9 || value >= 16) {
            if (markVoid)
                return new ItemStack(BuddycardsItems.VOID_ZYLEX.get(), value/9);
            return new ItemStack(BuddycardsItems.ZYLEX.get(), (value+2)/9);
        }
        return new ItemStack(BuddycardsItems.ZYLEX_NUGGET.get(), value);
    }
}
