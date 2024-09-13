package io.github.binboutachi.libs.minecraft.hud.renderables;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public abstract class Renderable<T> {
    // public record Holder<T>(T renderObject, float x, float y, int tint) {

    // }
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

    @SuppressWarnings("unchecked")
    public static <V extends Renderable<?>> V of(Type<V> t) {
        V retVal = null;
        switch (t.type) {
            case TEXT:
            case TEXT_SHADOW:
                retVal = (V) new TextRenderable();
            case TEXT_CENTERED:
            case TEXT_SHADOW_CENTERED:
                retVal = (V) new TextRenderable();
                retVal.centered = true;
                break;
        
            default:
                break;
        }
        return retVal;
    }
}
