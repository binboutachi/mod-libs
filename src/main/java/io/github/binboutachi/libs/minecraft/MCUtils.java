package io.github.binboutachi.libs.minecraft;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

    
public final class MCUtils {
    private MCUtils() {}
    
	private static Logger LOGGER = LogManager.getLogger(MCUtils.class);
    private static final File runDir = new File("").getAbsoluteFile();
    /**
     * This field is set to {@code MinecraftClient.getInstance()}
     * on class initialization. Useful for being shorter than
     * the alternative function call.
     */
    public static final MinecraftClient client = MinecraftClient.getInstance();

    /**
     * Returns whether the player currently plays single-player.
     * Note that this also returns {@code false} if there is no
     * active connection. In other words, using this as the sole
     * condition to otherwise do server connection-specific tasks
     * may not work and in most cases fail with an exception,
     * depending on where your code is called from.
     * It is therefore recommended to use this in conjunction with
     * {@link io.github.binboutachi.libs.minecraft.MCUtils#isConnectedToServer() isConnectedToServer()}.
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
        return client != null && client.getNetworkHandler() != null
            && client.getNetworkHandler().getConnection() != null;
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
     * Sends a local (non-networked) message to the player
     * in the specified formatting.
     * @param type the type of formatting to use. see the
     * {@link MessageType} enum class for available options.
     * @param msg the message to send
     */
    public static void sendLocalChatMessage(MessageType type, MutableText txt) {
        if(!isConnectedToServer()) {
            LOGGER.debug(String.format("Could not send message %s to local chat because there is no active connection.", txt.toString()));
            return;
        }
        client.inGameHud.getChatHud().addMessage(textWithColor(txt, type.color));
    }
    /**
     * Sends a local (non-networked) message to the player
     * in standard formatting. Its effect is the same as
     * if invoking {@code sendLocalChatMessage(MessageType.STANDARD, txt)}
     * @param type the type of formatting to use. see the
     * {@link MessageType} enum class for available options.
     * @param msg the message to send
     */
    public static void sendLocalChatMessage(MessageType type, String msg) {
        sendLocalChatMessage(type, Text.literal(msg));
    }
    /**
     * Sends a local (non-networked) message to the player
     * in standard formatting. Its effect is the same as
     * if invoking {@code sendLocalChatMessage(MessageType.STANDARD, Text.literal(msg))}
     * @param msg the message to send
     */
    public static void sendLocalChatMessage(String msg) {
        sendLocalChatMessage(MessageType.STANDARD, Text.literal(msg));
    }
    /**
     * Sends this local (non-networked) {@code MutableText}
     * to the player.
     * @param text the {@code MutableText} to send
     */
    public static void sendLocalChatMessage(MutableText text) {
        sendLocalChatMessage(MessageType.STANDARD, text);
    }
    public static String getTranslatedFromTypeAndId(String type, Identifier id) {
        return I18n.translate(Util.createTranslationKey(type, id));
    }
    /**
     * Changes the styling of the {@code text} such that
     * its color becomes the one specified in {@code color}
     * @param text the {@code MutableText} to modify
     * @param color the {@code Formatting} containing the
     * desired coloring
     * @return {@code text} with the new coloring applied
     */
    public static MutableText textWithColor(MutableText text, Formatting color) {
        var style = text.getStyle();
        text.setStyle(style.withColor(color));
        return text;
    }
}
