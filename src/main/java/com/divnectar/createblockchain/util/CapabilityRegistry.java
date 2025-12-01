package com.divnectar.createblockchain.util;

import com.divnectar.createblockchain.block.ModBlocks;
import com.divnectar.createblockchain.block.entity.CurrencyMinerBlockEntity;
import net.minecraft.core.Direction;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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

        // Register Forge Energy capability adapter at runtime (if Forge classes are present).
        // This uses reflection so the mod can compile without a hard dependency on Forge.
        try {
            Class<?> forgeCapsClass = Class.forName("net.minecraftforge.common.capabilities.ForgeCapabilities");
            Field energyField = forgeCapsClass.getField("ENERGY");
            Object forgeEnergyCap = energyField.get(null);

            event.registerBlockEntity(
                    forgeEnergyCap,
                    ModBlocks.CURRENCY_MINER_BE.get(),
                    (blockEntity, context) -> {
                        try {
                            Class<?> ieClass = Class.forName("net.minecraftforge.energy.IEnergyStorage");
                            Object energy = blockEntity.getEnergyStorage();

                            InvocationHandler handler = (proxy, method, args) -> {
                                String name = method.getName();
                                try {
                                    if (name.equals("receiveEnergy")) {
                                        Method m = energy.getClass().getMethod("receiveEnergy", int.class, boolean.class);
                                        return m.invoke(energy, args[0], args[1]);
                                    }
                                    if (name.equals("extractEnergy")) {
                                        Method m = energy.getClass().getMethod("extractEnergy", int.class, boolean.class);
                                        return m.invoke(energy, args[0], args[1]);
                                    }
                                    if (name.equals("getEnergyStored")) {
                                        Method m = energy.getClass().getMethod("getEnergyStored");
                                        return m.invoke(energy);
                                    }
                                    if (name.equals("getMaxEnergyStored")) {
                                        Method m = energy.getClass().getMethod("getMaxEnergyStored");
                                        return m.invoke(energy);
                                    }
                                    if (name.equals("canExtract")) {
                                        Method m = energy.getClass().getMethod("canExtract");
                                        return m.invoke(energy);
                                    }
                                    if (name.equals("canReceive")) {
                                        Method m = energy.getClass().getMethod("canReceive");
                                        return m.invoke(energy);
                                    }
                                } catch (NoSuchMethodException e) {
                                    // Fallthrough to null return
                                }
                                return null;
                            };

                            return Proxy.newProxyInstance(ieClass.getClassLoader(), new Class[]{ieClass}, handler);
                        } catch (Throwable t) {
                            return null;
                        }
                    }
            );
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ignored) {
            // Forge not present — nothing to do.
        }
    }
}