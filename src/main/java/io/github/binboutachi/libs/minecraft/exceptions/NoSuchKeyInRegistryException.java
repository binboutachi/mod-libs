package io.github.binboutachi.libs.minecraft.exceptions;

public final class NoSuchKeyInRegistryException extends Exception {
    public NoSuchKeyInRegistryException(String message) {
        super(message);
    }
    public NoSuchKeyInRegistryException(Throwable cause) {
        super(cause);
    }
    public NoSuchKeyInRegistryException(String message, Throwable cause) {
        super(message, cause);
    }
}
