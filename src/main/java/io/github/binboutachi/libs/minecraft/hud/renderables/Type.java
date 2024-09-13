package io.github.binboutachi.libs.minecraft.hud.renderables;

class Type<U extends Renderable<?>> {
    public final Typ type;
    enum Typ {
        TEXT,
        TEXT_CENTERED,
        TEXT_SHADOW,
        TEXT_SHADOW_CENTERED;
    }
    Type(Typ t) {
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