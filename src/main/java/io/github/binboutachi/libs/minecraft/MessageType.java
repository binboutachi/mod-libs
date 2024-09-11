package io.github.binboutachi.libs.minecraft;

import net.minecraft.util.Formatting;

public enum MessageType {
    STANDARD(Formatting.WHITE),
    WARNING(Formatting.YELLOW),
    ERROR(Formatting.DARK_RED),
    HALF_OPAQUE(Formatting.GRAY);

    public final Formatting color;
    MessageType(Formatting c) {
        color = c;
    }
}
