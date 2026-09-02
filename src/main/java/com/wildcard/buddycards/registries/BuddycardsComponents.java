package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.item.component.BinderContents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class BuddycardsComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Buddycards.MOD_ID);

    public static void registerComponents(IEventBus eventBus) {
        COMPONENTS.register(eventBus);
    }

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BUDDYCARD_FOIL = register("foil", i -> i.persistent(ExtraCodecs.NON_NEGATIVE_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BUDDYCARD_GRADE = register("grade", i -> i.persistent(ExtraCodecs.NON_NEGATIVE_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BinderContents>> BINDER = register("binder", i -> i.persistent(BinderContents.CODEC).networkSynchronized(BinderContents.STREAM_CODEC).cacheEncoding());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> SCANNER_COLLECTION = register("collection", i -> i.persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COLLECTION_TIER = register("tier", i -> i.persistent(ExtraCodecs.NON_NEGATIVE_INT));

    public static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }
}