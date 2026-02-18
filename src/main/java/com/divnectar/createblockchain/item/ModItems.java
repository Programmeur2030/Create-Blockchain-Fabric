package com.divnectar.createblockchain.item;

import com.divnectar.createblockchain.CreateBlockchain;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {

    // We no longer register our own currency, as we're using Create: Numismatics.
    // This is where you would register any other custom items your mod might have.
    // Example:
    // public static final Item MY_CUSTOM_ITEM = registerItem("my_custom_item", new Item(new Item.Properties()));


    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CreateBlockchain.MODID, name), item);
    }

    public static void register() {
        // This method is called to initialize the class and register the items.
    }
}
