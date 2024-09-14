package io.github.binboutachi.libs.async;

import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.lang.Thread.Builder.OfVirtual;
import java.util.concurrent.ThreadFactory;

import io.github.binboutachi.libs.async.conditions.ExecuteCondition;

public class ManagedThread {
    static final long CHECK_DELAY = 100l;
    private static final ManagedThreadBuilder builder = new ManagedThreadBuilder();
    static ThreadFactory factory;

    Runnable function;
    Consumer<ManagedThread> consumer;
    ExecuteCondition executeCondition;
    Consumer<Exception> onException;
    BiConsumer<Exception, ManagedThread> onExceptionBi;
    boolean cancelled = false;
    boolean paused = false;
    Thread thread;
    private boolean wasShutdown = false;

    static {
        OfVirtual builder = Thread.ofVirtual();
        builder.name("ManagedThread-", 0);
        factory = builder.factory();
    }

    Runnable f = () -> {
        while(!cancelled) {
            // println("executeCondition: " + executeCondition.isReached());
            try {
                if(executeCondition.isReached())
                    break;
                Thread.sleep(CHECK_DELAY);
            } catch (InterruptedException e) {
                if(wasShutdown)
                    return;
                if(onException != null)
                    onException.accept(e);
                return;
            } catch (Exception e) {
                // System.err.println("ManagedThread was interrupted during checking for its execution condition. The thread will now exit.");
                if(onException != null)
                    onException.accept(e);
                else if(onExceptionBi != null)
                    onExceptionBi.accept(e, this);
                return;
            }
        }
        if(cancelled)
            return;
        // System.out.println("Execute condition reached.");
        if(consumer != null)
            consumer.accept(this);
        else
            function.run();
    };

    ManagedThread(Runnable f, Consumer<ManagedThread> r, ExecuteCondition c, Consumer<Exception> onExc, BiConsumer<Exception, ManagedThread> onExcB) {
        function = f;
        consumer = r;
        executeCondition = c;
        onException = onExc;
        onExceptionBi = onExcB;
    }
    /**
     * Gets a {@code ManagedThreadBuilder}
     * instance. This method internally only returns
     * the same single instance, but always resets it
     * before returning it. If more than one
     * {@code ManagedThreadBuilder} is required at
     * the same time, use the public constructor of
     * {@code ManagedThreadBuilder} instead.
     */
    public static ManagedThreadBuilder builder() {
        builder.reset();
        return builder;
    }
    
    /**
     * Begins waiting for the {@code ExecutionCondition}.
     * More technically, this method starts execution of the
     * {@code VirtualThread} instance which performs
     * periodic checking for the {@code ExecuteCondition}.
     * If the {@code ExecuteCondition} supports refreshing
     * its state, it is also refreshed before starting
     * the backing thread.
     */
    public void start() {
        executeCondition.refresh();
        thread = factory.newThread(f);
        thread.start();
    }

    /**
     * Cancels this {@code ManagedThread} during
     * its waiting phase without interrupting the
     * backing thread. It has no effect when
     * called during the execution of its function.
     */
    public void cancelGracefully() {
        cancelled = true;
    }
    /**
     * Forcibly shuts the backing thread down
     * by interrupting it. The
     * {@code InterruptedException} is suppressed
     * when caused by a call to this method.
     */
    public void shutdown() {
        thread.interrupt();
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
