package io.github.binboutachi.libs;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class Gadgets {
	private Gadgets() {}
    private static Logger LOGGER = LogManager.getLogger(Gadgets.class);
    private static final File runDir = new File("").getAbsoluteFile();
    /**
     * A reference to the currently active {@code MinecraftClient}
     * instance.
     */
    public static MinecraftClient client;
    
    // public static boolean isConnectedToServer() {
    //     return AmbienceMod.instance.clientJoinDisconnectHandler().isConnectedToServer();
    // }
    // public static boolean isSinglePlayerServer() {
    //     return AmbienceMod.instance.clientJoinDisconnectHandler().isClient();
    // }
    /**
     * Returns whether the player currently plays single-player.
     * Note that this also returns {@code false} if there is no
     * active connection. In other words, using this as the sole
     * condition to otherwise do server connection-specific tasks
     * may not work and in most cases fail with an exception,
     * depending on where your code is called from.
     * It is therefore recommended to use this in conjunction with
     * {@link io.github.binboutachi.libs.ambiencemusic.util.Gadgets#isConnectedToServer() isConnectedToServer()}.
     * @return {@code true} if single-player, {@code false} if
     * not or no connection is active
     */
    public static boolean isSinglePlayerServer() {
        return MinecraftClient.getInstance().getServer() != null;
    }
    /**
     * Returns whether the client is currently connected to any
     * server, regardless of single- or multi-player. In other words,
     * this method returns whether the client is in-game.
     * @return {@code true} if a connection is active, {@code false}
     * if not
     */
    public static boolean isConnectedToServer() {
        return MinecraftClient.getInstance() != null && MinecraftClient.getInstance().getNetworkHandler() != null
            && MinecraftClient.getInstance().getNetworkHandler().getConnection() != null;
    }
    /**
     * Gets the current run directory. If no Minecraft instance
     * is running, the current working directory is returned
     * instead.
     */
    public static File runDir() {
        return runDir;
    }
    /**
     * <b>Callable only after having joined a world once!</b>
     * @return the {@code MinecraftClient} instance
     */
    public static net.minecraft.client.MinecraftClient minecraftClient() {
        return client;
    }
    public static void sendLocalChatMessage(String msg) {
        if(!isConnectedToServer()) {
            LOGGER.debug(String.format("Could not send message %s to local chat because there is no active connection.", msg));
            return;
        }
        Gadgets.client.player.sendMessage(Text.of(msg), false);
    }
}
