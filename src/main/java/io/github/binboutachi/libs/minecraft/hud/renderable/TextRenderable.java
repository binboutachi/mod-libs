package io.github.binboutachi.libs.minecraft.hud.renderable;

import io.github.binboutachi.libs.minecraft.MCUtils;

import net.minecraft.client.gui.DrawContext;

public class TextRenderable extends RenderableImpl<String, TextRenderable> {
    protected float fontSize = -1;
    
    TextRenderable() {
        textRenderer = MCUtils.client.textRenderer;
        renderObject = "UNINITIALIZED TEXTRENDERABLE";
    }

    @Override
    public void drawImpl(DrawContext drawContext) {
        drawContext.drawText(textRenderer, renderObject,
            Math.round(centered ? x - textRenderer.getWidth(renderObject) / 2 : x),
            Math.round(centered ? y - textRenderer.getWrappedLinesHeight(renderObject, Integer.MAX_VALUE) / 2 : y),
            tint,
            hasShadow);
    }
    @Override
    public String toString() {
        return "TextRenderable[Text=\"%s\", x-pos=%f, y-pos=%f, tint=%d, centered=%b, shadow=%b]"
            .formatted(renderObject, x, y, tint, centered, hasShadow);
    }
    @Override
    public TextRenderable x(float x) {
        this.x = x;
        return this;
    }
}
