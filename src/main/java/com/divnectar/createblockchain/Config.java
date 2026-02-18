package com.divnectar.createblockchain;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@me.shedaniel.autoconfig.annotation.Config(name = CreateBlockchain.MODID)
public class Config implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public int baseEnergyPerCoin = 830000;

    @ConfigEntry.Gui.Tooltip
    public int difficultyBonus = 10000;

    @ConfigEntry.Gui.Tooltip
    public int difficultyInterval = 110;

    @ConfigEntry.Gui.Tooltip
    public int maxEnergyConsumption = 4096;

    @ConfigEntry.Gui.Tooltip
    public int energyCapacity = 100000;

    @ConfigEntry.Gui.Tooltip
    public String coinToGenerate = "SPUR";

    public static Config get() {
        return AutoConfig.getConfigHolder(Config.class).getConfig();
    }

    public static void register() {
        AutoConfig.register(Config.class, GsonConfigSerializer::new);
    }
}
