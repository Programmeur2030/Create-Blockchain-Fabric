package com.divnectar.createblockchain.world;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.PersistentState;

public class CurrencyTracker extends PersistentState {

    private int totalMined = 0;
    private static final String TAG_NAME = "currency_tracker";

    public CurrencyTracker() {}

    public static CurrencyTracker load(CompoundTag tag, HolderLookup.Provider provider) {
        CurrencyTracker tracker = new CurrencyTracker();
        tracker.totalMined = tag.getInt("totalMined");
        return tracker;
    }

    public static CurrencyTracker get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new PersistentState.Factory<>(CurrencyTracker::new, CurrencyTracker::load, null), TAG_NAME);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putInt("totalMined", totalMined);
        return compoundTag;
    }

    public int getTotalMined() {
        return this.totalMined;
    }

    public void incrementMined() {
        this.totalMined++;
        setDirty();
    }
}
