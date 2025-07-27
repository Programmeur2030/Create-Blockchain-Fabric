/**
 * =================================================================================
 * NEW FILE - Sound Event Registration
 *
 * This class handles registering all custom sound events for the mod.
 *
 * File Path: src/main/java/com/divnectar/createblockchain/sound/ModSounds.java
 * =================================================================================
 */
package com.divnectar.createblockchain.sound;

import com.divnectar.createblockchain.CreateBlockchain;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, CreateBlockchain.MODID);

    // Register our custom coin sound event
    public static final DeferredHolder<SoundEvent, SoundEvent> COIN_CHACHING =
            SOUND_EVENTS.register("coin_chaching", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CreateBlockchain.MODID, "coin_chaching")));


    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
