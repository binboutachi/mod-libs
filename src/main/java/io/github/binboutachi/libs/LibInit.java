package io.github.binboutachi.libs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.binboutachi.libs.minecraft.MCUtils;
import io.github.binboutachi.libs.minecraft.world.WorldUtils;


public class LibInit implements ModInitializer {
	public static final String MOD_ID = "mod-libs";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final boolean DEBUG_ENABLED;

	static {
		if(LOGGER.isDebugEnabled())
			DEBUG_ENABLED = true;
		else
			DEBUG_ENABLED = true;
	}
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		
		LOGGER.info("Loaded binboutachi mod-libs.");
		if(!DEBUG_ENABLED) {
			return;
		}
		LOGGER.info("MCUtils.client: " + MCUtils.client);
		LOGGER.info("MinecraftClient.getInstance(): " + MinecraftClient.getInstance());
		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			if(!WorldUtils.isNthTick(20))
				return;
			if(client.player == null)
				return;
			MCUtils.sendLocalChatMessage("Biome name translated: " + MCUtils.getTranslatedFromTypeAndId("biome", WorldUtils.getBiomeIdByPos(client.player.getBlockPos())));
		});
	}
}