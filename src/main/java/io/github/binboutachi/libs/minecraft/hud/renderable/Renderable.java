package io.github.binboutachi.libs.minecraft.hud.renderable;

import net.minecraft.client.gui.DrawContext;

public interface Renderable<T> {
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
    public void draw(DrawContext drawContext);
    /**
     * This is the method to override for implementing
     * {@code Renderable}s. It should perform the necessary
     * rendering of the HUD element during the frame it is
     * called in. The public {@link #draw(DrawContext)
     * draw()} function takes care of fading in and out at
     * the beginning and end depending on the fade duration.
     * @param drawContext the context used for the actual
     * rendering of the HUD element and is required to
     * be non-{@code null}
     */
    void drawImpl(DrawContext drawContext);
    // public Type type();
    public float x();
    public float y();
    public int tint();
    public T renderObject();
    public boolean centered();
    public boolean castsShadow();
    public int fade();
    public boolean isRendering();
    public Renderable<T> positionAt(float x, float y);
    public Renderable<T> x(float x);
    public Renderable<T> y(float y);
    /**
     * <p> Tints the {@code Renderable} in the given
     * color. May have a smaller or larger effect than
     * expected depending on the type of {@code Renderable}.
     * A {@code TextRenderable}, for example, uses this
     * tint to fully color the text. </p>
     * The integer can be fed by providing a
     * hex number in the style of standard RBG-Hexcodes.
     * For example, pure opaque white is {@code 0xFFFFFF}.
     * If transparency is desired, the alpha value needs
     * to be prepended at the front like in the following
     * example for a half-transparent black tint:
     * {@code 0x88000000}. Note that HUD rendering for
     * some reason ignores alphas values of {@code 0x03}
     * and lower, instead drawing such elements with full
     * opacity.
     * @param tint the color to tint the {@code Renderable}
     * in
     * @return this {@code Renderable} instance
     */
    public Renderable<T> tint(int tint);
    public Renderable<T> renderObject(T renderObject);
    public Renderable<T> centered(boolean center);
    public Renderable<T> castsShadow(boolean castsShadow);
    /**
     * Sets the fade duration when this {@code Renderable}
     * is appearing or disappearing. A value smaller than
     * {@code 0} causes fading to be omitted.
     * @param durationMillis determines the duration of fading in
     * milliseconds
     * @return this {@code Renderable} instance
     */
    public Renderable<T> fade(int durationMillis);

    /**
     * Called on a {@code Renderable} when it is
     * first added to the HUD, notably before it
     * is added to the queue of {@code Renderable}s
     * to render.
     */
    public void onAddedToHud();
    /**
     * Called on a {@code Renderable} when its
     * standard render duration has ended.
     */
    public void onRenderDurationEnd();
    /**
     * Called on a {@code Renderable} when it is
     * removed from the HUD. Its {@code draw()}
     * method will not be called once this method
     * is invoked.
     */
    public void onRemovedFromHud();
    
    @SuppressWarnings("unchecked")
    public static <V extends Renderable<?>> V of(RenderableType<V> t) {
        V retVal = null;
        switch (t.type) {
            case TEXT:
                retVal = (V) new TextRenderable();
                break;
        
            default:
                break;
        }
        return retVal;
    }
}
