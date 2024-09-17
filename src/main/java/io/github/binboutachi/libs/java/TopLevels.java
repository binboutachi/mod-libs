package io.github.binboutachi.libs.java;

import java.util.function.Consumer;

import io.github.binboutachi.libs.async.ManagedThread;

/**
 * Provides useful static functions ready for
 * import.
 */
public final class TopLevels {
    private static final int MANAGED_THREAD_POOL_SIZE = 4;
    private static final ManagedThread[] managedThreads = new ManagedThread[MANAGED_THREAD_POOL_SIZE];
    private TopLevels() {}
    public static void runAsync(Runnable function) {
        
    }
    /**
     * Repeats {@code function} {@code count} times, while
     * {@code function} is able to act on the running
     * index, which starts with {@code 0}, counting up
     * to including {@code count - 1}. This function just
     * wraps {@code function} in a standard C-style for-loop.
     * @param count the amount to repeat {@code function}
     * @param function the function to repeat, which also
     * gets the running index on each iteration
     */
    public static void repeat(int count, Consumer<Integer> function) {
        for(int i = 0; i < count; i++) {
            function.accept(i);
        }
    }
    /**
     * Repeats {@code function} {@code count} times.
     * This function just wraps {@code function} in a
     * standard C-style for-loop.
     * @param count the amount to repeat {@code function}
     * @param function the function to repeat
     */
    public static void repeat(int count, Runnable function) {
        for(int i = 0; i < count; i++) {
            function.run();
        }
    }
}
