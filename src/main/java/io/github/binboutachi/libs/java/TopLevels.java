package io.github.binboutachi.libs.java;

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
}
