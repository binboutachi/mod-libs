package io.github.binboutachi.libs.minecraft.world;

import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.binboutachi.libs.minecraft.MCUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public final class WorldUtils {
    private WorldUtils() {}
    private static Logger LOGGER = LogManager.getLogger(WorldUtils.class);
    /**
     * If the current world is a single-player world, return its save
     * path, if not, return {@code null}.
     * @return the save path of the current world if it is a
     * single-player world, {@code null} otherwise.
     */
    public static Path getCurrentWorldSavePath() {
        try(MinecraftClient client = MinecraftClient.getInstance()) {
            // ClientWorld clientWorld = client.world;
            if(MCUtils.isConnectedToServer() && MCUtils.isSinglePlayerServer()) {
                LOGGER.info("Successfully got world save path");
                return client.getServer().getSavePath(WorldSavePath.ROOT);
            }
        } catch(Exception e) {
            LOGGER.warn("IOException in getCurrentWorldSavePath()");
        }
        return null;
    }
    /**
	 * Returns {@code true} once every {@code nthTick} on the currently
	 * connected server. The tick count used for comparison is determined
	 * either by
	 * <ol>
	 * 	<li>the integrated server running during single player</li>
	 * 	<li>the server world's global time</li>
	 * </ol>
	 * This means that for single player worlds, since the game connects
	 * to an internal server, the amount of ticks performed in one second
	 * might not be the expected 20, especially if performance problems
	 * are encountered. This also extends to the 3rd-party server the
	 * client might be connected to, since it can also experience similar
	 * issues. In other words, this method should not be used to time
	 * critical events that happen once every real-time time interval,
	 * such as once every second. For these use cases, consider using
	 * {@link java.lang.System#currentTimeMillis()} instead.
	 * @param nthTick the number of ticks to skip + 1
     * @return {@code true} if the current tick is the nth the cumulative
     * ticks processed so far by the world, {@code false} if not
	 */
	public static boolean isNthTick(int nthTick) {
		try (MinecraftClient client = MinecraftClient.getInstance()) {
			if(MCUtils.isSinglePlayerServer()) {
				return client.getServer().getTicks() % nthTick == 0;
			} else {
				//LOGGER.info("socket address: " + ((InetSocketAddress) thiz.networkHandler.getConnection().getAddress()));
				return client.world.getTime() % nthTick == 0;
			}
		} catch(Exception e) {
			LOGGER.error(e.getMessage());
            LOGGER.error("Failed to calculate the modular of the current tick.");
		}
        return false;
	}
    /**
     * The current world. Might be {@code null} if not connected
     * to a (integrated) server. 
     */
	@SuppressWarnings("resource")
    public static net.minecraft.world.World cWorld() {
		return MinecraftClient.getInstance().world;
	}
    public static String getBiomeNameByPos(net.minecraft.util.math.Vec3d pos) {
        Text biomeName = Text.of(Util.createTranslationKey("biome", getBiomeIdByPos(pos)));
        return biomeName.getString();
    }
    public static Identifier getBiomeIdByPos(net.minecraft.util.math.Vec3d pos) {
        java.util.Optional<RegistryKey<Biome>> biomeHolder = cWorld().getBiomeKey(new BlockPos(pos.x, pos.y, pos.z));
        try {
            return biomeHolder.get().getValue();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error("Failed in getting identifier of the biome; the BiomeHolder had no key contained to retrieve.");
        }
        return new Identifier("minecraft", "plains");
    }
    public static Biome getBiomeByPos(BlockPos pos) {
        return cWorld().getBiome(pos);
    }
}
