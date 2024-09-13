package io.github.binboutachi.libs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.binboutachi.libs.minecraft.hud.HudRenderCallbackHandler;


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
		
		HudRenderCallback.EVENT.register(HudRenderCallbackHandler.singleton);
		
		LOGGER.info("Loaded binboutachi mod-libs.");
	}
}