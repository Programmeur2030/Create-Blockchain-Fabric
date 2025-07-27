package com.divnectar.createblockchain.util;

import com.divnectar.createblockchain.block.ModBlocks;
import com.divnectar.createblockchain.block.entity.CurrencyMinerBlockEntity;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CapabilityRegistry {

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Register Energy Capability
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlocks.CURRENCY_MINER_BE.get(),
                (blockEntity, context) -> {
                    // Only provide the energy capability on the EAST and WEST sides.
                    if (context == Direction.EAST || context == Direction.WEST) {
                        return blockEntity.getEnergyStorage();
                    }
                    // For any other side (including null), provide nothing.
                    return null;
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
    }
}