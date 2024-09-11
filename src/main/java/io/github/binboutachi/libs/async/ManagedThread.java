package io.github.binboutachi.libs.async;

import java.util.function.Consumer;

import java.util.concurrent.ThreadFactory;

import io.github.binboutachi.libs.async.conditions.ExecuteCondition;

public class ManagedThread {
    static final long CHECK_DELAY = 100l;

    Runnable function;
    ExecuteCondition executeCondition;
    Consumer<Exception> onException;
    boolean cancelled = false;
    boolean paused = false;
    ThreadFactory factory;

    Runnable f = () -> {
        while(!cancelled) {
            // println("executeCondition: " + executeCondition.isReached());
            try {
                if(executeCondition.isReached())
                    break;
                Thread.sleep(CHECK_DELAY);
            } catch (Exception e) {
                // System.err.println("ManagedThread was interrupted during checking for its execution condition. The thread will now exit.");
                if(onException != null)
                    onException.accept(e);
                return;
            }
        }
        // System.out.println("Execute condition reached.");
        function.run();
    };

    ManagedThread(Runnable f, ExecuteCondition c, Consumer<Exception> onExc) {
        function = f;
        executeCondition = c;
        onException = onExc;
        var builder = Thread.ofVirtual();
        builder.name("ManagedThread", 0);
        factory = builder.factory();
    }
    
    /**
     * Begins waiting for the {@code ExecutionCondition}.
     * More technically, this method starts execution of the
     * {@code VirtualThread} instance which performs
     * periodic checking for the {@code ExecuteCondition}.
     */
    public void start() {
        executeCondition.refresh();
        factory.newThread(f).start();
    }

    public void cancelGracefully() {
        cancelled = true;
    }
    /**
     * Pauses the {@code ExecuteCondition}.
     */
    public void pause() {
        executeCondition.pause();
    }
    /**
     * Unpauses the {@code ExecuteCondition}.
     */
    public void unpause() {
        executeCondition.unpause();
    }
}
