package com.divnectar.createblockchain.item;

import com.divnectar.createblockchain.CreateBlockchain;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    // Updated for 1.21.1: Must specify the registry type.
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, CreateBlockchain.MODID);

    // We no longer register our own currency, as we're using Create: Numismatics.
    // This is where you would register any other custom items your mod might have.

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}