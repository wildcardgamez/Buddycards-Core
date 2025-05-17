package com.wildcard.buddycards.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import java.util.function.Supplier;

public class LootInjectionModifier extends LootModifier {
    public static final Supplier<Codec<LootInjectionModifier>> SERIALIZER = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ResourceLocation.CODEC.fieldOf("injection").forGetter((m) -> m.injection))
                    .apply(inst, LootInjectionModifier::new)));

    private final ResourceLocation injection;

    protected LootInjectionModifier(LootItemCondition[] conditionsIn, ResourceLocation injection) {
        super(conditionsIn);
        this.injection = injection;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        LootTable table = context.getResolver().getLootTable(injection);
        generatedLoot.addAll(table.getRandomItems(new LootParams.Builder(context.getLevel()).create(LootContextParamSets.EMPTY)));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return SERIALIZER.get();
    }
}
