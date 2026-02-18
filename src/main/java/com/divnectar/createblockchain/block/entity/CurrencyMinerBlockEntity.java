package com.divnectar.createblockchain.block.entity;

import com.divnectar.createblockchain.Config;
import com.divnectar.createblockchain.block.ModBlocks;
import com.divnectar.createblockchain.sound.ModSounds;
import com.divnectar.createblockchain.world.CurrencyTracker;
import com.mojang.logging.LogUtils;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import dev.ithundxr.createnumismatics.content.backend.Coin;
import net.fabricmc.fabric.api.transfer.v1.energy.EnergyStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.List;

public class CurrencyMinerBlockEntity extends BlockEntity implements IHaveGoggleInformation, SidedStorageBlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    public final SimpleEnergyStorage energyStorage;
    public final SimpleContainer inventory = new SimpleContainer(1);
    private final InventoryStorage inventoryStorage = InventoryStorage.of(inventory, null);


    private long energyToMine;
    private long accumulatedEnergy = 0;
    private int lastEnergyConsumed = 0;

    public CurrencyMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.CURRENCY_MINER_BE, pos, state);
        this.energyStorage = new SimpleEnergyStorage(Config.get().energyCapacity, Config.get().maxEnergyConsumption, Config.get().maxEnergyConsumption) {
            @Override
            protected void onFinalCommit() {
                setChanged();
            }
        };
        this.energyToMine = Config.get().baseEnergyPerCoin;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CurrencyMinerBlockEntity be) {
        if (level.isClientSide) {
            return;
        }

        be.updateMiningCost();

        long energyToConsume = be.energyStorage.extract(Config.get().maxEnergyConsumption, true);

        if (energyToConsume > 0) {
            be.energyStorage.extract(energyToConsume, false);
            be.accumulatedEnergy += energyToConsume;
        }

        if (be.accumulatedEnergy >= be.energyToMine) {
            int coinsToMine = (int) (be.accumulatedEnergy / be.energyToMine);
            for (int i = 0; i < coinsToMine; i++) {
                be.mineCoin();
            }
            be.accumulatedEnergy %= be.energyToMine;
        }

        if (be.lastEnergyConsumed != energyToConsume || level.getGameTime() % 20 == 0) {
            be.lastEnergyConsumed = (int) energyToConsume;
            be.setChanged();
        }
    }

    private void mineCoin() {
        if (this.level instanceof ServerLevel serverLevel) {
            CurrencyTracker.get(serverLevel).incrementMined();

            String coinTypeName = Config.get().coinToGenerate.toUpperCase();
            Coin coinToMine;
            try {
                coinToMine = Coin.valueOf(coinTypeName);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Invalid coin type '{}' in config. Defaulting to SPUR.", coinTypeName);
                coinToMine = Coin.SPUR;
            }

            ItemStack coinStack = new ItemStack(coinToMine.asStack().getItem());
            serverLevel.playSound(null, this.worldPosition, ModSounds.COIN_CHACHING, SoundSource.BLOCKS, 0.5f, 1.5f);
            this.inventory.addItem(coinStack);
        }
    }

    private void updateMiningCost() {
        if (this.level instanceof ServerLevel serverLevel) {
            int totalMined = CurrencyTracker.get(serverLevel).getTotalMined();
            long difficultyBonus = (long) Math.floor((double) totalMined / Config.get().difficultyInterval) * Config.get().difficultyBonus;
            long newEnergyToMine = Config.get().baseEnergyPerCoin + difficultyBonus;

            if (this.energyToMine != newEnergyToMine) {
                this.energyToMine = newEnergyToMine;
                setChanged();
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putLong("accumulatedEnergy", this.accumulatedEnergy);
        tag.putLong("energy", energyStorage.getAmount());
        tag.put("inventory", inventory.createTag(provider));
        tag.putInt("lastEnergyConsumed", this.lastEnergyConsumed);
        tag.putLong("energyToMine", this.energyToMine);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.accumulatedEnergy = tag.getLong("accumulatedEnergy");
        energyStorage.amount = tag.getLong("energy");
        inventory.fromTag(tag.getList("inventory", CompoundTag.TAG_COMPOUND), provider);
        this.lastEnergyConsumed = tag.getInt("lastEnergyConsumed");
        this.energyToMine = tag.getLong("energyToMine");
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(Component.translatable("goggle.createblockchain.info.header"));
        tooltip.add(Component.literal("  ")
                .append(Component.translatable("goggle.createblockchain.info.usage"))
                .append(Component.literal(": " + this.lastEnergyConsumed + " FE/t").withStyle(ChatFormatting.AQUA)));

        double coinsPerMinute = 0;
        if (this.lastEnergyConsumed > 0) {
            double ticksPerCoin = (double) this.energyToMine / this.lastEnergyConsumed;
            coinsPerMinute = (1200.0) / ticksPerCoin;
        }
        tooltip.add(Component.literal("  ")
                .append(Component.translatable("goggle.createblockchain.info.rate"))
                .append(Component.literal(String.format(": %.2f Coins/min", coinsPerMinute)).withStyle(ChatFormatting.GREEN)));

        tooltip.add(Component.literal("  ")
                .append(Component.translatable("goggle.createblockchain.info.cost"))
                .append(Component.literal(": " + this.energyToMine + " FE").withStyle(ChatFormatting.GOLD)));

        ItemStack storedStack = this.inventory.getItem(0);
        tooltip.add(Component.literal("  ")
                .append(Component.translatable("goggle.createblockchain.info.stored"))
                .append(Component.literal(": " + storedStack.getCount() + " ").append(storedStack.getHoverName()).withStyle(ChatFormatting.WHITE)));

        return true;
    }

    @Nullable
    @Override
    public EnergyStorage getEnergyStorage(Direction side) {
        if (side == Direction.EAST || side == Direction.WEST) {
            return energyStorage;
        }
        return null;
    }

    @Nullable
    @Override
    public InventoryStorage getItemStorage(Direction side) {
        if (side == Direction.DOWN) {
            return inventoryStorage;
        }
        return null;
    }
}
