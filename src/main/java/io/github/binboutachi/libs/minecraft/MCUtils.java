package io.github.binboutachi.libs.minecraft;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

    
public final class MCUtils {
    private MCUtils() {}
    
	private static Logger LOGGER = LogManager.getLogger(MCUtils.class);
    private static final File runDir = new File("").getAbsoluteFile();
    public static final MinecraftClient client = MinecraftClient.getInstance();

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
    public static void sendLocalChatMessage(String msg) {
        if(!isConnectedToServer()) {
            LOGGER.debug(String.format("Could not send message %s to local chat because there is no active connection.", msg));
            return;
        }
        try(MinecraftClient client = MinecraftClient.getInstance()) {
            client.player.sendMessage(Text.of(msg), false);
        } catch (Exception e) {}
    }
    /**
     * Shorthand for calling
     * {@code MinecraftClient.getInstance()}
     * @return the {@code MinecraftClient} instance
     */
    public static MinecraftClient client() {
        return MinecraftClient.getInstance();
    }
}
