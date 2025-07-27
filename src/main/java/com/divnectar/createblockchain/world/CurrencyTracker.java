package com.divnectar.createblockchain.world;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class CurrencyTracker extends SavedData {

    private int totalMined = 0;
    private static final String TAG_NAME = "currency_tracker";

    private CurrencyTracker() {}

    private CurrencyTracker(CompoundTag tag) {
        this.totalMined = tag.getInt("totalMined");
    }

    public static CurrencyTracker load(CompoundTag tag, HolderLookup.Provider provider) {
        return new CurrencyTracker(tag);
    }

    public static CurrencyTracker get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(CurrencyTracker::new, CurrencyTracker::load, null), TAG_NAME);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, @NotNull HolderLookup.Provider provider) {
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