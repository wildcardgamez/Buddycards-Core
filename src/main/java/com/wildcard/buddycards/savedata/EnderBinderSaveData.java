package com.wildcard.buddycards.savedata;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.container.BinderContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderBinderSaveData extends SavedData {
    private final static HashMap<UUID, BinderContainer> INVENTORIES = new HashMap<>();

    public EnderBinderSaveData() {
    }

    public EnderBinderSaveData(CompoundTag nbt) {
        ListTag list = nbt.getList("ebdata", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag compound = list.getCompound(i);
            BinderContainer inv = new BinderContainer(54, true);
            inv.fromTag(compound.getList("inv", CompoundTag.TAG_COMPOUND));
            INVENTORIES.put(compound.getUUID("uuid"), inv);
        }
    }

    public static EnderBinderSaveData get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(EnderBinderSaveData::new, EnderBinderSaveData::new, Buddycards.MOD_ID + "_ender_binders");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, BinderContainer> i: INVENTORIES.entrySet()) {
            CompoundTag compound = new CompoundTag();
            compound.putUUID("uuid", i.getKey());
            compound.put("inv", i.getValue().createTag());
            list.add(compound);
        }
        nbt.put("ebdata", list);
        return nbt;
    }

    public BinderContainer getOrMakeEnderBinder(UUID uuid) {
        if(!INVENTORIES.containsKey(uuid))
            INVENTORIES.put(uuid, new BinderContainer(54, true));
        setDirty();
        return(INVENTORIES.get(uuid));
    }
}