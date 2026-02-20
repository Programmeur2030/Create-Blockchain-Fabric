package com.divnectar.createblockchain.block;

import com.divnectar.createblockchain.CreateBlockchain;
import com.divnectar.createblockchain.block.entity.CurrencyMinerBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModBlocks {

    public static final Block GEM_POLISHING_STATION = registerBlock("gem_polishing_station",
            new CurrencyMinerBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));

    //public static final Item CURRENCY_MINER_ITEM = registerBlockItem("currency_miner", CURRENCY_MINER);

    //public static final BlockEntityType<CurrencyMinerBlockEntity> CURRENCY_MINER_BE =
            //Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    //new ResourceLocation(CreateBlockchain.MODID, "currency_miner_be"),
                    //BlockEntityType.Builder.of(CurrencyMinerBlockEntity::new, CURRENCY_MINER).build(null));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(CreateBlockchain.MODID, name), block);
    }
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(CreateBlockchain.MODID, name),
                new BlockItem(block, new FabricItemSettings()));
    }


    public static void register() {
        // This method is called to initialize the class and register the blocks and block entities.
    }
}
