package com.divnectar.createblockchain.item;

import com.divnectar.createblockchain.CreateBlockchain;


public class ModItems {

    // We no longer register our own currency, as we're using Create: Numismatics.
    // This is where you would register any other custom items your mod might have.
    // Example:
    // public static final Item MY_CUSTOM_ITEM = registerItem("my_custom_item", new Item(new Item.Properties()));



    public static void register() {
        // This method is called to initialize the class and register the items.
        CreateBlockchain.LOGGER.info("no items regestring for CreateBlockchain");
    }
}
