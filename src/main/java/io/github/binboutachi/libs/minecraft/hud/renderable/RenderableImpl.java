package io.github.binboutachi.libs.minecraft.hud.renderable;

import java.util.ArrayList;

import io.github.binboutachi.libs.async.ManagedThread;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

abstract class RenderableImpl<T> implements Renderable<T> {
    T renderObject;
    TextRenderer textRenderer;
    protected boolean centered;
    protected boolean hasShadow;
    protected float x;
    protected float y;
    protected int tint = 0xFFFFFF;
    int originalTint = tint;
    protected ArrayList<ManagedThread> colorTransitions = new ArrayList<>();
    boolean beingRendered = false;
    int fadeDuration = 0;

    protected RenderableImpl() {
        
    }
    protected void stopAllColorTransitions() {
        for(int i = 0; i < colorTransitions.size(); i++) {
            colorTransitions.remove(i).shutdown();
            i--;
        }
    }

    public void drawInFade(DrawContext drawContext, int startTint, int endTint) {
        
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
        return this.fadeDuration;
    }
    public Renderable<T> positionAt(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }
    public Renderable<T> x(float x) {
        return positionAt(x, y);
    }
    public Renderable<T> y(float y) {
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
    public Renderable<T> tint(int tint) {
        this.tint = tint;
        this.originalTint = tint;
        return this;
    }
    public Renderable<T> renderObject(T renderObject) {
        this.renderObject = renderObject;
        return this;
    }
    public Renderable<T> centered(boolean center) {
        centered = center;
        return this;
    }
    public Renderable<T> castsShadow(boolean castsShadow) {
        hasShadow = castsShadow;
        return this;
    }
    public Renderable<T> fade(int durationMillis) {
        this.fadeDuration = durationMillis;
        return this;
    }
}
