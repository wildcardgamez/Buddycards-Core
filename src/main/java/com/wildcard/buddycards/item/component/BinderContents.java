package com.wildcard.buddycards.item.component;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Stream;

//This whole class is mostly just a copy of ItemContainerContents since extension wasn't an option and I needed more space
public final class BinderContents {
    private static final int NO_SLOT = -1;
    private static final int MAX_SIZE = 1024;
    public static final BinderContents EMPTY = new BinderContents(NonNullList.create());
    public static final Codec<BinderContents> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, BinderContents> STREAM_CODEC;
    private final NonNullList<ItemStack> items;
    private final int hashCode;

    private BinderContents(NonNullList<ItemStack> items) {
        if (items.size() > 1024) {
            throw new IllegalArgumentException("Got " + items.size() + " items, but maximum is 1024");
        } else {
            this.items = items;
            this.hashCode = ItemStack.hashStackList(items);
        }
    }

    private BinderContents(int size) {
        this(NonNullList.withSize(size, ItemStack.EMPTY));
    }

    private BinderContents(List<ItemStack> items) {
        this(items.size());

        for(int i = 0; i < items.size(); ++i) {
            this.items.set(i, items.get(i));
        }

    }

    private static BinderContents fromSlots(List<BinderContents.Slot> slots) {
        OptionalInt optionalint = slots.stream().mapToInt(BinderContents.Slot::index).max();
        if (optionalint.isEmpty()) {
            return EMPTY;
        } else {
            BinderContents BinderContents = new BinderContents(optionalint.getAsInt() + 1);

            for(BinderContents.Slot BinderContents$slot : slots) {
                BinderContents.items.set(BinderContents$slot.index(), BinderContents$slot.item());
            }

            return BinderContents;
        }
    }

    public static BinderContents fromItems(List<ItemStack> items) {
        int i = findLastNonEmptySlot(items);
        if (i == -1) {
            return EMPTY;
        } else {
            BinderContents BinderContents = new BinderContents(i + 1);

            for(int j = 0; j <= i; ++j) {
                BinderContents.items.set(j, items.get(j).copy());
            }

            return BinderContents;
        }
    }

    private static int findLastNonEmptySlot(List<ItemStack> items) {
        for(int i = items.size() - 1; i >= 0; --i) {
            if (!items.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    private List<BinderContents.Slot> asSlots() {
        List<BinderContents.Slot> list = new ArrayList();

        for(int i = 0; i < this.items.size(); ++i) {
            ItemStack itemstack = this.items.get(i);
            if (!itemstack.isEmpty()) {
                list.add(new BinderContents.Slot(i, itemstack));
            }
        }

        return list;
    }

    public void copyInto(NonNullList<ItemStack> list) {
        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = i < this.items.size() ? (ItemStack)this.items.get(i) : ItemStack.EMPTY;
            list.set(i, itemstack.copy());
        }

    }

    public ItemStack copyOne() {
        return this.items.isEmpty() ? ItemStack.EMPTY : ((ItemStack)this.items.get(0)).copy();
    }

    public Stream<ItemStack> stream() {
        return this.items.stream().map(ItemStack::copy);
    }

    public Stream<ItemStack> nonEmptyStream() {
        return this.items.stream().filter((p_331322_) -> !p_331322_.isEmpty()).map(ItemStack::copy);
    }

    public Iterable<ItemStack> nonEmptyItems() {
        return Iterables.filter(this.items, (p_331420_) -> !p_331420_.isEmpty());
    }

    public Iterable<ItemStack> nonEmptyItemsCopy() {
        return Iterables.transform(this.nonEmptyItems(), ItemStack::copy);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            if (other instanceof BinderContents) {
                BinderContents BinderContents = (BinderContents)other;
                if (ItemStack.listMatches(this.items, BinderContents.items)) {
                    return true;
                }
            }

            return false;
        }
    }

    public int hashCode() {
        return this.hashCode;
    }

    public int getSlots() {
        return this.items.size();
    }

    public ItemStack getStackInSlot(int slot) {
        this.validateSlotIndex(slot);
        return ((ItemStack)this.items.get(slot)).copy();
    }

    private void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.getSlots()) {
            throw new UnsupportedOperationException("Slot " + slot + " not in valid range - [0," + this.getSlots() + ")");
        }
    }

    static {
        CODEC = BinderContents.Slot.CODEC.sizeLimitedListOf(1024).xmap(BinderContents::fromSlots, BinderContents::asSlots);
        STREAM_CODEC = ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list(1024)).map(BinderContents::new, (p_331691_) -> p_331691_.items);
    }

    static record Slot(int index, ItemStack item) {
        public static final Codec<BinderContents.Slot> CODEC = RecordCodecBuilder.create((p_331695_) -> p_331695_.group(Codec.intRange(0, 1023).fieldOf("slot").forGetter(BinderContents.Slot::index), ItemStack.CODEC.fieldOf("item").forGetter(BinderContents.Slot::item)).apply(p_331695_, BinderContents.Slot::new));
    }
}
