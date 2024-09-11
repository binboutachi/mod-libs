package io.github.binboutachi.libs.async;

import java.util.function.Consumer;

import io.github.binboutachi.libs.async.conditions.ExecuteCondition;

public class ManagedThread extends Thread {
    Runnable function;
    ExecuteCondition executeCondition;
    Consumer<Exception> onException;
    boolean cancelled = false;
    boolean paused = false;

    static final long CHECK_DELAY = 100l;

    ManagedThread(Runnable f, ExecuteCondition c, Consumer<Exception> onExc) {
        super();
        function = f;
        executeCondition = c;
        onException = onExc;
    }

    
    @Override
    public void run() {
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
        f.run();
    }

    public void cancelGracefully() {
        cancelled = true;
    }
    /**
     * Pauses the {@code ExecuteCondition}.
     */
    public void managedPause() {
        executeCondition.pause();
    }
    /**
     * Resumes the {@code ExecuteCondition}.
     */
    public void managedResume() {
        executeCondition.unpause();
    }
}
