package io.github.binboutachi.libs.minecraft.hud.renderables;

import io.github.binboutachi.libs.minecraft.hud.renderables.Type.Typ;

public class Types {
    public static final Type<TextRenderable> TEXT = new Type<>(Typ.TEXT);
    public static final Type<TextRenderable> TEXT_CENTERED = new Type<>(Typ.TEXT_CENTERED);
    public static final Type<TextRenderable> TEXT_SHADOW = new Type<>(Typ.TEXT_SHADOW);
    public static final Type<TextRenderable> TEXT_SHADOW_CENTERED = new Type<>(Typ.TEXT_SHADOW_CENTERED);
}