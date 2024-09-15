package io.github.binboutachi.libs.minecraft.hud.renderable;

class RenderableType<U extends Renderable<?>> {
    public final Typ type;
    enum Typ {
        TEXT;
    }
    RenderableType(Typ t) {
        type = t;
    }

    // public final BiConsumer<DrawContext, MutableText> function;
    // Type(BiConsumer<DrawContext> f) {
    //     function = f;
    // }
    // final Renderable render;
    // Type(Renderable r) {
    //     render = r;
    // }
}