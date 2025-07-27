package com.divnectar.createblockchain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    public static final ModConfigSpec.IntValue BASE_ENERGY_PER_COIN = BUILDER
            .comment("The base amount of Forge Energy (FE) required to mine one coin. Default value is around 1 coin per min with two max speed alternators.")
            .defineInRange("baseEnergyPerCoin", 830000, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue DIFFICULTY_BONUS = BUILDER
            .comment("How much additional FE is added to the mining cost each time the difficulty increases.")
            .defineInRange("difficultyBonus", 10000, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue DIFFICULTY_INTERVAL = BUILDER
            .comment("How many coins must be mined globally before the mining cost increases by the difficulty bonus.")
            .defineInRange("difficultyInterval", 110, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue MAX_ENERGY_CONSUMPTION = BUILDER
            .comment("The maximum amount of FE the miner can consume per tick.")
            .defineInRange("maxEnergyConsumption", 4096, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue ENERGY_CAPACITY = BUILDER
            .comment("The total amount of FE the miner can store internally.")
            .defineInRange("energyCapacity", 100000, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.ConfigValue<String> COIN_TO_GENERATE = BUILDER
            .comment("The type of coin to generate when mining. Can be SPUR, BEVEL, SPROCKET, COG, CROWN, or SUN.")
            .define("coinToGenerate", "SPUR");



    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
