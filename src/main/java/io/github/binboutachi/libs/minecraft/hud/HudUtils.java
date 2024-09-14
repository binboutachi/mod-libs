package io.github.binboutachi.libs.minecraft.hud;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.binboutachi.libs.LibInit;
import io.github.binboutachi.libs.async.ManagedThread;
import io.github.binboutachi.libs.minecraft.hud.renderable.Renderable;
import io.github.binboutachi.libs.minecraft.hud.renderable.TextRenderable;
import io.github.binboutachi.libs.minecraft.hud.renderable.Type;

public class HudUtils {
	private static Logger LOGGER = LogManager.getLogger();
    static final int DEFAULT_RENDER_DURATION = 5000;
    private static ArrayList<ManagedThread> removalThreads = new ArrayList<>(6);
    
    /**
     * Renders the {@code Renderable} instance for {@code durationMillis}
     * milliseconds on the game's HUD.
     * @param renderable the object representing what to render onto the
     * HUD. see {@link io.github.binboutachi.libs.minecraft.hud.renderable.Type Types}
     * for a list of available options and the
     * {@link io.github.binboutachi.libs.minecraft.hud.renderable.Renderable#of(io.github.binboutachi.libs.minecraft.hud.renderable.Type)
     * Renderable.of(Type)} method to get a {@code Renderable} instance
     * and modify it
     * @param durationMillis the duration to render {@code renderable} in
     * milliseconds. specify {@code -1} for infinite duration (as in as
     * long as connected to the current server).
     */
    public static void render(Renderable<?> renderable, int durationMillis) {
        HudRenderCallbackHandler.singleton.addRenderable(renderable);
        if(durationMillis == -1) // do not schedule a removal of the HUD element in case of infinite duration
            return;
        
        final ManagedThread thread = ManagedThread.builder()
            .withFunction(thiz -> {
                HudRenderCallbackHandler.singleton.removeRenderable(renderable);
                if(LibInit.DEBUG_ENABLED)
                    LOGGER.info("Removed Renderable from HUD.");
                removalThreads.remove(thiz);
            })
            .withDelay(durationMillis)
            .withExceptionHandler((e, thiz) -> {
                LOGGER.error("Waiting for the duration to render a HUD element was interrupted by an exception, thus the HUD element was destroyed prematurely.");
                HudRenderCallbackHandler.singleton.removeRenderable(renderable);
                removalThreads.remove(thiz);
            }).buildAndStart();
        removalThreads.add(thread);
    }
    public static void render(Renderable<?> renderable) {
        render(renderable, DEFAULT_RENDER_DURATION);
    }
    public static void renderTextAt(String text, int x, int y, int durationMillis) {
        render(Renderable.of(Type.TEXT).positionAt(x, y).renderObject(text), durationMillis);
    }
    public static void renderTextAt(String text, int x, int y) {
        render(Renderable.of(Type.TEXT).positionAt(x, y).renderObject(text), DEFAULT_RENDER_DURATION);
    }
}
