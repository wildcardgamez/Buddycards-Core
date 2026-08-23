package com.wildcard.buddycards.entity;

import com.mojang.datafixers.util.Pair;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

import java.util.*;

public class EnderlingEntity extends PathfinderMob implements Npc, Nameable {
    final ArrayList<Pair<ItemStack, ItemStack>> goalTrades = new ArrayList<>();
    boolean cheap;
    int timer = 0;

    public EnderlingEntity(EntityType<? extends PathfinderMob> type, Level lvl) {
        super(type, lvl);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        cheap = lvl.getRandom().nextDouble() < .1F;
        setCanPickUpLoot(true);
        if (lvl instanceof ServerLevel serverLevel)
            setupGoalItems(serverLevel);
    }

    public static AttributeSupplier setupAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, .5D)
                .add(Attributes.FOLLOW_RANGE, 6.0f)
                .build();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1f));
        this.goalSelector.addGoal(2, new TemptGoal(this, .75f, Ingredient.of(BuddycardsItems.ZYLEX.get(), BuddycardsItems.VOID_ZYLEX.get()), false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, .5f, 0.0f));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 8.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FollowMobGoal(this, 0.6f, 1.5f, 6.0f));
    }

    @Override
    public int getBaseExperienceReward() {
        return 1 + this.level().random.nextInt(3);
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

    @Override
    protected void customServerAiStep() {
        if (this.level().isDay() && this.tickCount >= 600) {
            float f = this.getLightLevelDependentMagicValue();
            if (f > 0.5F && this.level().canSeeSky(this.blockPosition()) && this.random.nextFloat() * 60.0F < (f - 0.4F) * 2.0F) {
                this.teleport();
            }
        }
        super.customServerAiStep();
    }

    protected boolean teleport() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 32.0D;
            double d1 = this.getY() + this.random.nextInt(32) - 32;
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 32.0D;
            return this.teleport(d0, d1, d2);
        } else
            return false;
    }

    private boolean teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(x, y, z);
        while(blockpos$mutable.getY() > 0 && !this.level().getBlockState(blockpos$mutable).blocksMotion())
            blockpos$mutable.move(Direction.DOWN);
        BlockState blockstate = this.level().getBlockState(blockpos$mutable);
        if (blockstate.blocksMotion() && !blockstate.getFluidState().is(Fluids.WATER)) {
            EntityTeleportEvent.EnderEntity event = EventHooks.onEnderTeleport(this, x, y, z);
            if (event.isCanceled()) return false;
            Vec3 vec3 = this.position();
            boolean success = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (success) {
                this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
                if (!this.isSilent()) {
                    this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }
            return success;
        } else
            return false;
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide)
            for (int i = 0; i < 2; ++i)
                this.level().addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
        if (timer > 0) {
            timer -= 1;
            if (timer == 0) {
                if (getMainHandItem().getItem().equals(Items.PAPER)) {
                    ItemStack note = new ItemStack(Items.PAPER, 1);
                    List<Component> lore = new ArrayList<>();
                    for (Pair<ItemStack, ItemStack> goalTrade : goalTrades) {
                        ItemStack card = goalTrade.getFirst();
                        if (card.getItem() instanceof BuddycardItem cardItem) {
                            MutableComponent component = Component.translatable(cardItem.getSet().getDescriptionId())
                                    .append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.number_separator")
                                            .append(Component.translatable(cardItem.getDescriptionId())));
                            int foil = card.get(BuddycardsComponents.BUDDYCARD_FOIL), grade = card.get(BuddycardsComponents.BUDDYCARD_GRADE);
                            if (foil > 0)
                                component.append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.foil_symbol").withStyle(foil == 1 ? ChatFormatting.YELLOW : foil == 2 ? ChatFormatting.GOLD : ChatFormatting.LIGHT_PURPLE));
                            if (grade > 0) {
                                component.append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.number_separator")
                                        .append(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.grade." + grade)));
                            }
                            lore.add(component);
                        }
                    }
                    if(hasCustomName()) {
                        MutableComponent component = Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.number_separator");
                        lore.add(component.append(getCustomName()));
                    }
                    note.set(DataComponents.LORE, new ItemLore(lore));
                    Vec3 pos = position().add(0, 1, 0);
                    Player player = level().getNearestPlayer(this, 5);
                    if (player != null)
                        pos = player.position().add(0, 1, 0);
                    BehaviorUtils.throwItem(this, note, pos);
                    setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

                } else
                    for (Pair<ItemStack, ItemStack> goalTrade : goalTrades)
                        if (cardsMatch(goalTrade.getFirst(), getMainHandItem())) {
                            Vec3 pos = position().add(0, 1, 0);
                            Player player = level().getNearestPlayer(this, 5);
                            if (player != null)
                                pos = player.position().add(0, 1, 0);
                            BehaviorUtils.throwItem(this, goalTrade.getSecond(), pos);
                            setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                            goalTrades.remove(goalTrade);
                            if (goalTrade.getFirst().getItem().equals(BuddycardsItems.ZYLEX.get()))
                                goalTrades.add(new Pair<>(new ItemStack(BuddycardsItems.ZYLEX.get()), getBarterResult(level(), false)));
                            if (goalTrade.getFirst().getItem().equals(BuddycardsItems.VOID_ZYLEX.get()))
                                goalTrades.add(new Pair<>(new ItemStack(BuddycardsItems.VOID_ZYLEX.get()), getBarterResult(level(), true)));
                            else
                                addSingleCardTrade(level());
                            break;
                        }
            }
        }
        super.aiStep();
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    public void setupGoalItems(Level lvl) {
        if (!goalTrades.isEmpty())
            return;
        RandomSource rand = lvl.getRandom();
        List<BuddycardItem> cards = BuddycardsAPI.getAllCards().stream().filter(BuddycardItem::shouldLoad).toList();
        for (int i = 0; i < 10; i++) {
            ItemStack card = new ItemStack(cards.get(rand.nextInt(cards.size())));
            if (i % 3 == 0)
                BuddycardItem.setShiny(card, rand.nextInt(1,3));
            goalTrades.add(new Pair<>(card, getCardSellValue(card, rand, cheap)));
            if (i < 3) {
                ItemStack card2 = card.copy();
                card2.set(BuddycardsComponents.BUDDYCARD_GRADE, rand.nextInt(2,5));
                goalTrades.add(new Pair<>(card2, getCardSellValue(card2, rand, cheap)));
            }
        }
        goalTrades.add(new Pair<>(new ItemStack(BuddycardsItems.ZYLEX.get()), getBarterResult(level(), false)));
        goalTrades.add(new Pair<>(new ItemStack(BuddycardsItems.VOID_ZYLEX.get()), getBarterResult(level(), true)));
    }

    public void addSingleCardTrade(Level lvl) {
        RandomSource rand = lvl.getRandom();
        List<BuddycardItem> cards = BuddycardsAPI.getAllCards().stream().filter(BuddycardItem::shouldLoad).toList();
        ItemStack card = new ItemStack(cards.get(rand.nextInt(cards.size())));
        goalTrades.add(new Pair<>(card, getCardSellValue(card, rand, cheap)));
    }

    static final ResourceKey<LootTable> ZYLEX_BARTER = ResourceKey.create(Registries.LOOT_TABLE, Buddycards.buddycardsLocation("gameplay/zylex_barter"));
    static final ResourceKey<LootTable> VOID_ZYLEX_BARTER = ResourceKey.create(Registries.LOOT_TABLE, Buddycards.buddycardsLocation("gameplay/void_zylex_barter"));

    public static ItemStack getBarterResult(Level level, boolean voidZylex) {
        if (level instanceof ServerLevel lvl) {
            LootTable table = lvl.getServer().reloadableRegistries().getLootTable(voidZylex ? VOID_ZYLEX_BARTER : ZYLEX_BARTER);
            List<ItemStack> items = table.getRandomItems((new LootParams.Builder(lvl)).create(LootContextParamSets.EMPTY));
            System.out.println(items);
            return items.getFirst();
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getCardSellValue(ItemStack card, RandomSource rand, boolean cheap) {
        int value = rand.nextInt(1, 4);
        boolean markVoid = false;
        if (card.getRarity() == Rarity.RARE)
            value += 4;
        if (card.getRarity() == Rarity.EPIC)
            value += 8;
        if (card.has(BuddycardsComponents.BUDDYCARD_FOIL)) {
            int foil = card.get(BuddycardsComponents.BUDDYCARD_FOIL);
            if (foil == 1)
                value += 4;
            else if (foil == 2)
                value += 6;
            else if (foil == 3)
                value *= 3;
        }
        if(card.has(BuddycardsComponents.BUDDYCARD_GRADE)) {
            int grade = card.get(BuddycardsComponents.BUDDYCARD_GRADE);
            if (grade == 1)
                value -= 2;
            else if (grade == 2)
                value += 4;
            else if (grade == 3)
                value *= 2;
            else if (grade == 4)
                value *= 4;
            else if (grade == 5)
                value *= 25;
            if(grade >= 2 || card.getRarity() == Rarity.EPIC)
                markVoid = true;
        }
        if (cheap)
            value /= 2;
        value = Math.max(1, value);
        if (value >= 9) {
            if(rand.nextInt(3) == 0)
                markVoid = true;
            if (markVoid)
                return new ItemStack(BuddycardsItems.VOID_ZYLEX.get(), value/9);
            return new ItemStack(BuddycardsItems.ZYLEX.get(), value/9);
        }
        return new ItemStack(BuddycardsItems.ZYLEX_NUGGET.get(), value);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(timer == 0 && goalTrades.stream().anyMatch((i) -> cardsMatch(i.getFirst(), player.getItemInHand(hand)))) {
            setItemInHand(InteractionHand.MAIN_HAND, player.getMainHandItem().split(1));
            timer = 200;
        }
        if(player.getItemInHand(hand).getItem().equals(Items.PAPER)) {
            setItemInHand(InteractionHand.MAIN_HAND, player.getMainHandItem().split(1));
            timer = 200;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("TradeTimer", timer);
        tag.putBoolean("Cheap", cheap);
        ListTag tradesTag = new ListTag();
        for(Pair<ItemStack, ItemStack> trade : goalTrades) {
            CompoundTag tradeTag = new CompoundTag();
                tradeTag.put("Goal", trade.getFirst().saveOptional(this.registryAccess()));
            tradeTag.put("Reward", trade.getSecond().saveOptional(this.registryAccess()));
            tradesTag.add(tradeTag);
        }
        tag.put("GoalTrades", tradesTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        timer = tag.getInt("TradeTimer");
        cheap = tag.getBoolean("Cheap");
        goalTrades.clear();
        for(Tag tradeTag : tag.getList("GoalTrades", Tag.TAG_COMPOUND))
            goalTrades.add(new Pair<>(ItemStack.parseOptional(this.registryAccess(), ((CompoundTag) tradeTag).getCompound("Goal")), ItemStack.parseOptional(this.registryAccess(), ((CompoundTag) tradeTag).getCompound("Reward"))));
    }

    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        return goalTrades.stream().anyMatch((i) -> cardsMatch(i.getFirst(), stack));
    }

    @Override
    protected void pickUpItem(ItemEntity item) {
        if (getMainHandItem().isEmpty()) {
            this.onItemPickup(item);
            this.take(item, 1);
            setItemInHand(InteractionHand.MAIN_HAND, item.getItem().split(1));
            timer = 200;
        }
    }

    static boolean cardsMatch(ItemStack a, ItemStack b) {
        return a.getItem().equals(b.getItem()) && BuddycardItem.getFoil(a) == BuddycardItem.getFoil(b) && BuddycardItem.getGrade(a) == BuddycardItem.getGrade(b);
    }

    public boolean hurt(DamageSource source, float amt) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            boolean flag = source.getDirectEntity() instanceof ThrownPotion;
            if (!source.is(DamageTypeTags.IS_PROJECTILE) && !flag) {
                boolean flag2 = super.hurt(source, amt);
                if (!this.level().isClientSide() && !(source.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
                    this.teleport();
                }

                return flag2;
            } else {
                boolean flag1 = flag && this.hurtWithCleanWater(source, (ThrownPotion)source.getDirectEntity(), amt);

                for(int i = 0; i < 64; ++i) {
                    if (this.teleport()) {
                        return true;
                    }
                }

                return flag1;
            }
        }
    }

    private boolean hurtWithCleanWater(DamageSource source, ThrownPotion potion, float amount) {
        ItemStack itemstack = potion.getItem();
        PotionContents potioncontents = itemstack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return potioncontents.is(Potions.WATER) && super.hurt(source, amount);
    }
}
