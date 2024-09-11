package io.github.binboutachi.libs.async;


import java.util.function.Consumer;

import io.github.binboutachi.libs.async.conditions.ExecuteCondition;
import io.github.binboutachi.libs.async.conditions.TimedCondition;

public final class ManagedThreadBuilder {
    private ExecuteCondition condition;
    private Runnable f;
    private Consumer<Exception> onException;
    public ManagedThreadBuilder() { }

    public ManagedThread build() {
        if(f == null)
            throw new IllegalStateException("No function for the ManagedThread to run was specified.");
        return new ManagedThread(f, condition, onException);
    }
    public ManagedThread buildAndStart() {
        ManagedThread mT = build();
        mT.executeCondition.refresh();
        mT.start();
        return mT;
    }
    public void reset() {
        f = null;
        condition = null;
        onException = null;
    }
    public ManagedThreadBuilder withFunction(Runnable func) {
        f = func;
        return this;
    }
    public ManagedThreadBuilder withDelay(long d) {
        condition = new TimedCondition(d);
        return this;
    }
    public ManagedThreadBuilder withExceptionHandler(Consumer<Exception> onExc) {
        onException = onExc;
        return this;
    }
    // public <T> Builder withCondition(ExecuteCondition<T> c) {
    //     condition = c;
    //     return this;
    // }
}
