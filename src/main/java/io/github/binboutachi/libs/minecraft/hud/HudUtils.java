package io.github.binboutachi.libs.minecraft.hud;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.binboutachi.libs.LibInit;
import io.github.binboutachi.libs.async.AsyncUtils;
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
     * {@link io.github.binboutachi.libs.minecraft.hud.renderable.RenderableImpl#of(io.github.binboutachi.libs.minecraft.hud.renderable.Type)
     * Renderable.of(Type)} method to get a {@code Renderable} instance
     * and modify it
     * @param durationMillis the duration to render {@code renderable} in
     * milliseconds. specify {@code -1} for infinite duration (as in as
     * long as connected to the current server). setting {@durationMillis}
     * to {@code 0} results in no rendering taking place.
     * @return a {@code RenderableState} to observe the state of the
     * {@code Renderable} and stop its rendering at any point via a
     * callback. a duration of {@code 0} milliseconds leads to {@code null}
     * being returned instead.
     */
    public static RenderableState render(Renderable<?> renderable, int durationMillis) {
        if(durationMillis == 0)
            return null;
        renderable.onAddedToHud();
        HudRenderCallbackHandler.singleton.addRenderable(renderable);
        if(durationMillis == -1) // do not schedule a removal of the HUD element in case of infinite duration
            return new RenderableState(renderable, null);
        
        final ManagedThread thread = ManagedThread.builder()
            .withFunction(thiz -> {
                renderable.onRenderDurationEnd();
                try {
                    Thread.sleep(renderable.fade());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LOGGER.error("Waiting for fade to finish was interrupted.");
                }
                HudRenderCallbackHandler.singleton.removeRenderable(renderable);
                renderable.onRemovedFromHud();
                if(LibInit.DEBUG_ENABLED)
                    LOGGER.info("Removed Renderable from HUD.");
                removalThreads.remove(thiz);
            })
            .withDelay(durationMillis + renderable.fade())
            .withExceptionHandler((e, thiz) -> {
                LOGGER.error("Waiting for the duration to render a HUD element was interrupted by an exception, thus the HUD element was destroyed prematurely.");
                HudRenderCallbackHandler.singleton.removeRenderable(renderable);
                removalThreads.remove(thiz);
            }).buildAndStart();
        removalThreads.add(thread);
        return new RenderableState(renderable, thread);
    }
    public static RenderableState render(Renderable<?> renderable) {
        return render(renderable, DEFAULT_RENDER_DURATION);
    }
    public static RenderableState renderTextAt(String text, int x, int y, int durationMillis) {
        return render(Renderable.of(Type.TEXT).positionAt(x, y).renderObject(text), durationMillis);
    }
    public static RenderableState renderTextAt(String text, int x, int y) {
        return renderTextAt(text, x, y, DEFAULT_RENDER_DURATION);
    }
}
