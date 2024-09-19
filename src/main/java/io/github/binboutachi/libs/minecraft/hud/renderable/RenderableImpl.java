package io.github.binboutachi.libs.minecraft.hud.renderable;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.binboutachi.libs.async.ManagedThread;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

abstract class RenderableImpl<T, U extends Renderable<T, U>> implements Renderable<T, U> {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected static final int FADE_MOVE_AMOUNT_Y = -99999;
    
    T renderObject;
    TextRenderer textRenderer;
    protected boolean centered;
    protected boolean hasShadow;
    protected float x;
    protected float y;
    protected int tint = 0xFFFFFFFF;
    int originalTint = tint;
    protected ArrayList<ManagedThread> colorTransitions = new ArrayList<>();
    boolean beingRendered = false;
    protected float fadeDuration = 0;
    protected long fadeTimestamp = 0l;
    protected int fadeStart;
    protected int fadeGoal;
    boolean finishedFade = true;

    protected RenderableImpl() {
        
    }
    protected void stopAllColorTransitions() {
        for(int i = 0; i < colorTransitions.size(); i++) {
            colorTransitions.remove(i).shutdown();
            i--;
        }
    }

    public void draw(DrawContext drawContext) {
        if(fadeDuration > 0l && !finishedFade) {
            final int s_a = 0xFF & (fadeStart >> 24);
            final int s_r = 0xFF & (fadeStart >> 16);
            final int s_g = 0xFF & (fadeStart >> 8);
            final int s_b = 0xFF & (fadeStart);
            final int e_a = 0xFF & (fadeGoal  >> 24);
            final int e_r = 0xFF & (fadeGoal  >> 16);
            final int e_g = 0xFF & (fadeGoal  >> 8);
            final int e_b = 0xFF & (fadeGoal);
            final float inc_a = (e_a - s_a) / fadeDuration;
            final float inc_r = (e_r - s_r) / fadeDuration;
            final float inc_g = (e_g - s_g) / fadeDuration;
            final float inc_b = (e_b - s_b) / fadeDuration;
            final long cTime = System.currentTimeMillis();
            final int c_a = Math.round(s_a + (inc_a * (cTime - fadeTimestamp)));
            final int c_r = Math.round(s_r + (inc_r * (cTime - fadeTimestamp)));
            final int c_g = Math.round(s_g + (inc_g * (cTime - fadeTimestamp)));
            final int c_b = Math.round(s_b + (inc_b * (cTime - fadeTimestamp)));
            tint = (c_a << 24) | (c_r << 16) | (c_g << 8) | c_b;
            if(Math.abs(c_a - e_a) <= 3 && Math.abs(c_r - e_r) <= 3
            && Math.abs(c_g - e_g) <= 3 && Math.abs(c_b - e_b) <= 3) {
                finishedFade = true;
                tint = fadeGoal;
                fadeTimestamp -= fadeDuration;
            }
        }
        drawImpl(drawContext);
    }
    // public Type type();
    public float x() {
        return x;
    }
    public float y() {
        return y;
    }
    public int tint() {
        return tint;
    }
    public T renderObject() {
        return renderObject;
    }
    public boolean centered() {
        return centered;
    }
    public boolean castsShadow() {
        return hasShadow;
    }
    public int fade() {
        return (int) this.fadeDuration;
    }
    public boolean isRendering() {
        return this.beingRendered;
    }
    @SuppressWarnings("unchecked")
    public U positionAt(float x, float y) {
        this.x = x;
        this.y = y;
        return (U) this;
    }
    public U x(float x) {
        return positionAt(x, y);
    }
    public U y(float y) {
        return positionAt(x, y);
    }
    /**
     * <p> Tints the {@code Renderable} in the given
     * color. May have a smaller or larger effect than
     * expected depending on the type of {@code Renderable}.
     * A {@code TextRenderable} uses this tint to fully
     * color the text. </p>
     * The integer can be fed by providing a
     * hex number in the style of standard RBG-Hexcodes.
     * For example, pure opaque white is {@code 0xFFFFFF}.
     * If transparency is desired, the alpha value needs
     * to be prepended at the front like in the following
     * example for a half-transparent black tint:
     * {@code 0x88_000000}.
     * @param tint the color to tint the {@code Renderable}
     * in
     * @return this {@code Renderable} instance
     */
    @SuppressWarnings("unchecked")
    public U tint(int tint) {
        this.tint = tint;
        this.originalTint = tint;
        return (U) this;
    }
    @SuppressWarnings("unchecked")
    public U renderObject(T renderObject) {
        this.renderObject = renderObject;
        return (U) this;
    }
    @SuppressWarnings("unchecked")
    public U centered(boolean center) {
        centered = center;
        return (U) this;
    }
    @SuppressWarnings("unchecked")
    public U castsShadow(boolean castsShadow) {
        hasShadow = castsShadow;
        return (U) this;
    }
    @SuppressWarnings("unchecked")
    public U fade(int durationMillis) {
        this.fadeDuration = durationMillis;
        return (U) this;
    }

    public void onAddedToHud() {
        if(!(fadeDuration > 0))
            return;
        fadeStart = 0x04000000 | (originalTint & 0x00FFFFFF);
        // tint = fadeStart;
        fadeGoal = originalTint;
        fadeTimestamp = System.currentTimeMillis();
        finishedFade = false;
        beingRendered = true;
    }
    public void onRenderDurationEnd() {
        if(!(fadeDuration > 0))
            return;
        fadeStart = originalTint;
        // tint = fadeStart;
        fadeGoal = 0x04000000 | (originalTint & 0x00FFFFFF);
        fadeTimestamp = System.currentTimeMillis();
        finishedFade = false;
    }
    public void onRemovedFromHud() {
        beingRendered = false;
    }
}
