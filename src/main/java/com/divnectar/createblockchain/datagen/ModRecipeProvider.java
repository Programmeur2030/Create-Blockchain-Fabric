package com.divnectar.createblockchain.datagen;

import com.divnectar.createblockchain.CreateBlockchain;
import com.divnectar.createblockchain.block.ModBlocks;
import com.simibubi.create.content.processing.recipe.MechanicalCraftingRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<FinishedRecipe> exporter) {
        MechanicalCraftingRecipeBuilder.shapedRecipe(ModBlocks.CURRENCY_MINER)
                .patternLine("AAAAAA")
                .patternLine("ABEEBA")
                .patternLine("AILCIA")
                .patternLine("ABWWBA")
                .patternLine("AAAAAA")
                .key('A', BuiltInRegistries.ITEM.get(new ResourceLocation("create", "andesite_alloy")))
                .key('B', BuiltInRegistries.ITEM.get(new ResourceLocation("create", "brass_casing")))
                .key('E', BuiltInRegistries.ITEM.get(new ResourceLocation("create", "electron_tube")))
                .key('L', BuiltInRegistries.ITEM.get(new ResourceLocation("create", "redstone_link")))
                .key('C', BuiltInRegistries.ITEM.get(new ResourceLocation("numismatics", "crown")))
                .key('I', BuiltInRegistries.ITEM.get(new ResourceLocation("createaddition", "iron_wire")))
                .key('W', BuiltInRegistries.ITEM.get(new ResourceLocation("createaddition", "copper_wire")))
                .build(exporter, new ResourceLocation(CreateBlockchain.MODID, "mechanical_crafting/currency_miner"));
    }
}
