package io.github.binboutachi.libs.minecraft.hud;

import io.github.binboutachi.libs.async.ManagedThread;
import io.github.binboutachi.libs.minecraft.hud.renderable.Renderable;

public class RenderableState {
    private final ManagedThread thread;
    private final Renderable<?> renderable;

    RenderableState(Renderable<?> r, ManagedThread t) {
        renderable = r;
        thread = t;
    }
    /**
     * Returns whether the {@code Renderable} associated
     * with this {@code RenderableState} is currently
     * rendering. Note that this currently also reports
     * {@code true} if the game is paused, i.e. if the
     * game is technically not rendering because of
     * an open game screen (like the pause screen). As
     * long as the {@code Renderable} would continue to
     * render after closing the screen, this method will
     * return {@code true}.
     * @implNote if the {@code Renderable} was not
     * setup to automatically stop rendering after some
     * duration, this method will always return
     * {@code true}.
     */
    public boolean isRendering() {
        if(thread == null)
            return true;
        return thread.isDone();
    }
    /**
     * Immediately stops rendering the {@code Renderable}
     * associated with this {@code RenderableState}.
     */
    public void stopRendering() {
        HudRenderCallbackHandler.singleton.removeRenderable(renderable);
        thread.shutdown();
    }
}
