package com.divnectar.createblockchain.block;

import com.divnectar.createblockchain.CreateBlockchain;
import com.divnectar.createblockchain.block.entity.CurrencyMinerBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {

    public static final Block CURRENCY_MINER = registerBlock("currency_miner",
            new CurrencyMinerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0f).requiresCorrectToolForDrops().noOcclusion()));

    public static final Item CURRENCY_MINER_ITEM = registerBlockItem("currency_miner", CURRENCY_MINER);

    public static final BlockEntityType<CurrencyMinerBlockEntity> CURRENCY_MINER_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    new ResourceLocation(CreateBlockchain.MODID, "currency_miner_be"),
                    BlockEntityType.Builder.of(CurrencyMinerBlockEntity::new, CURRENCY_MINER).build(null));

    private static Block registerBlock(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(CreateBlockchain.MODID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CreateBlockchain.MODID, name),
                new BlockItem(block, new Item.Properties()));
    }

    public static void register() {
        // This method is called to initialize the class and register the blocks and block entities.
    }
}
