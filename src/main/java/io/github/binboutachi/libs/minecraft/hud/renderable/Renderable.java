package io.github.binboutachi.libs.minecraft.hud.renderable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public abstract class Renderable<T> {
    // public record Holder<T>(T renderObject, float x, float y, int tint) {

    // }
    private static class Test {
        private int test;
    }
    T renderObject;
    TextRenderer textRenderer;
    protected boolean centered;
    protected boolean hasShadow;
    protected float x;
    protected float y;
    protected int tint = 0xFFFFFF;

    protected Renderable() {
        textRenderer = MinecraftClient.getInstance().textRenderer;
    }
    /**
     * Performs the rendering of this {@code Renderable}
     * for the current frame it is called in. The implementation
     * depends on the implementing type of {@code Renderable}.
     * For example, {@code TextRenderable} draws a
     * {@code String} as a {@code Text} with the specified
     * properties.
     * @param drawContext the context used for the actual
     * rendering of the HUD element and is required to
     * be non-{@code null}
     */
    public abstract void draw(DrawContext drawContext);
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
    public Renderable<T> tint(int tint) {
        this.tint = tint;
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
        hasShadow = true;
        return this;
    }

    @SuppressWarnings("unchecked")
    public static <V extends Renderable<?>> V of(RenderableType<V> t) {
        V retVal = null;
        switch (t.type) {
            case TEXT:
                retVal = (V) new TextRenderable();
                retVal = (V) new TextRenderable();
                break;
        
            default:
                break;
        }
        return retVal;
    }
}
