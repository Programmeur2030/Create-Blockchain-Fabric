package com.divnectar.createblockchain.util;

import com.divnectar.createblockchain.block.ModBlocks;
import com.divnectar.createblockchain.block.entity.CurrencyMinerBlockEntity;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.Nullable;

public class CapabilityRegistry {

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Register Energy Capability
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlocks.CURRENCY_MINER_BE.get(),
                (blockEntity, context) -> {
                    // For null context (internal queries, like Create: New Age),
                    // return the raw delegate so they get full access (detection works).
                    // For directional sides (like Mekanism/Pipez connectors),
                    // wrap it to restrict input to EAST/WEST only.
                    if (context == null) {
                        return blockEntity.getEnergyStorage();
                    }
                    blockEntity.setQueriedSide(context);
                    return new DirectionalEnergyWrapper(blockEntity.getEnergyStorage(), blockEntity, context);
                }
        );

        // Register Item Handler Capability
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlocks.CURRENCY_MINER_BE.get(),
                (blockEntity, context) -> {
                    // Only provide the item handler capability on the SOUTH side.
                    if (context == Direction.SOUTH) {
                        return blockEntity.getItemHandler();
                    }
                    // For any other side, provide nothing.
                    return null;
                }
        );

        // NOTE: Avoiding a direct runtime registration of Forge's IEnergyStorage here because
        // the neoforge `RegisterCapabilitiesEvent.registerBlockEntity` API expects a
        // `BlockCapability<T,C>` from neoforge. Attempting to pass a reflected Forge capability
        // object caused a compile-time type mismatch. If Forge compatibility is required,
        // implement a separate runtime adapter that registers via Forge's event bus (using
        // reflection) or add a compile-time optional module that depends on Forge.
    }

    /**
     * Wrapper around EnergyStorage that restricts energy input to specific sides.
     * Allows external mods to detect the energy capability on all sides (for compatibility),
     * but only permits energy input from horizontal sides (NORTH, SOUTH, EAST, WEST).
     */
    private static class DirectionalEnergyWrapper extends EnergyStorage {
        private final CurrencyMinerBlockEntity blockEntity;
        @Nullable
        private final Direction side;

        public DirectionalEnergyWrapper(EnergyStorage delegate, CurrencyMinerBlockEntity blockEntity, @Nullable Direction side) {
            // Create a new energy storage with the same capacity and max I/O as the delegate
            super(delegate.getMaxEnergyStored(), delegate.getMaxEnergyStored(), delegate.getMaxEnergyStored(), delegate.getEnergyStored());
            this.blockEntity = blockEntity;
            this.side = side;
            // Copy state from delegate
            this.energy = delegate.getEnergyStored();
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            // Only allow energy input from valid sides
            if (!blockEntity.canInputEnergyFromSide(side)) {
                return 0;
            }
            // Delegate to the actual energy storage
            return blockEntity.getEnergyStorage().receiveEnergy(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            // Energy can always be extracted (no directional restriction)
            return blockEntity.getEnergyStorage().extractEnergy(maxExtract, simulate);
        }

        @Override
        public int getEnergyStored() {
            return blockEntity.getEnergyStorage().getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored() {
            return blockEntity.getEnergyStorage().getMaxEnergyStored();
        }

        @Override
        public boolean canExtract() {
            return blockEntity.getEnergyStorage().canExtract();
        }

        @Override
        public boolean canReceive() {
            return blockEntity.getEnergyStorage().canReceive() && blockEntity.canInputEnergyFromSide(side);
        }
    }
}