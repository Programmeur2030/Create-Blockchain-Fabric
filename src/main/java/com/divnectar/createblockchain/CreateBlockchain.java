package com.divnectar.createblockchain;

import com.divnectar.createblockchain.block.ModBlocks;
import com.divnectar.createblockchain.item.ModItems;
import com.divnectar.createblockchain.sound.ModSounds;
import com.mojang.logging.LogUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class CreateBlockchain implements ModInitializer {
    public static final String MODID = "createblockchain";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Create: Blockchain for Fabric");

        AutoConfig.register(Config.class, GsonConfigSerializer::new);

        ModItems.register();
        //ModBlocks.register();
        //ModSounds.register();
    }
}
