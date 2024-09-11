package io.github.binboutachi.libs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.binboutachi.libs.async.ManagedThreadBuilder;
import io.github.binboutachi.libs.minecraft.MCUtils;
import io.github.binboutachi.libs.minecraft.MessageType;
import io.github.binboutachi.libs.minecraft.world.WorldUtils;


public class LibInit implements ModInitializer {
	public static final String MOD_ID = "mod-libs";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static boolean DEBUG_ENABLED;
	private boolean once = false;

	static {
		if(LOGGER.isDebugEnabled())
			DEBUG_ENABLED = true;
		else
			DEBUG_ENABLED = false;
	}
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		
		LOGGER.info("Loaded binboutachi mod-libs.");
		DEBUG_ENABLED = true;
		if(!DEBUG_ENABLED) {
			return;
		}
		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			if(!WorldUtils.isNthTick(20))
				return;
			if(client.player == null)
				return;
			if(!once) {
				once = true;
				MCUtils.sendLocalChatMessage("World save path: " + WorldUtils.getCurrentWorldSavePath());
				MCUtils.sendLocalChatMessage(MessageType.STANDARD, "Standard");
				MCUtils.sendLocalChatMessage(MessageType.WARNING, "Warning");
				MCUtils.sendLocalChatMessage(MessageType.ERROR, "Error");
				MCUtils.sendLocalChatMessage(MessageType.HALF_OPAQUE, "Half opaque");
				var builder = new ManagedThreadBuilder();
				builder
					.withFunction(() -> {
						MCUtils.sendLocalChatMessage("done");
					})
					.withDelay(32000)
					.withExceptionHandler((e) -> {
						MCUtils.sendLocalChatMessage("Oops!\n" + e.getMessage());
					})
					.buildAndStart();
			}
			MCUtils.sendLocalChatMessage("Biome name translated: " + MCUtils.getTranslatedFromTypeAndId("biome", WorldUtils.getBiomeIdByPos(client.player.getBlockPos())));
		});
	}
}