package com.divnectar.createblockchain.block.entity;

import com.divnectar.createblockchain.Config;
import com.divnectar.createblockchain.CreateBlockchain;
import com.divnectar.createblockchain.block.ModBlocks;
import com.divnectar.createblockchain.sound.ModSounds;
import com.divnectar.createblockchain.world.CurrencyTracker;
import com.mojang.logging.LogUtils;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import dev.ithundxr.createnumismatics.content.backend.Coin;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;

import java.util.List;

public class CurrencyMinerBlockEntity extends BlockEntity implements IHaveGoggleInformation, SidedStorageBlockEntity {
    private static final Logger LOGGER = CreateBlockchain.LOGGER;


    public CurrencyMinerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


}
