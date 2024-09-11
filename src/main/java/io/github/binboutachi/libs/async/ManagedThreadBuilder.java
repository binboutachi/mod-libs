package io.github.binboutachi.libs.async;


import java.util.function.Consumer;

import io.github.binboutachi.libs.async.conditions.ExecuteCondition;
import io.github.binboutachi.libs.async.conditions.TimedCondition;

public final class ManagedThreadBuilder {
    private ExecuteCondition condition;
    private Runnable f;
    private Consumer<Exception> onException;
    public ManagedThreadBuilder() { }

    /**
     * Builds a {@code ManagedThread} based on
     * the state of this builder. It is recommended
     * to use {@link #buildAndStart()} instead
     * whenever possible at exactly the point in time,
     * at which the {@code ManagedThread} should begin
     * waiting for its {@code ExecuteCondition}.
     * @return the {@code ManagedThread} instance
     * based on the state of this builder
     */
    public ManagedThread build() {
        if(f == null)
            throw new IllegalStateException("No function for the ManagedThread to run was specified.");
        return new ManagedThread(f, condition, onException);
    }
    /**
     * Convenience method to build and subsequently start
     * a {@code ManagedThread} from this builder.
     * This is the preferred method to build
     * because it refreshes the contained
     * {@code ExecuteCondition}. In other words, it is
     * recommended to only build the {@code ManagedThread}
     * whenever it should start waiting for its
     * {@code ExecuteCondition}.
     * @return the {@code ManagedThread} instance
     * based on the state of this builder
     */
    public ManagedThread buildAndStart() {
        ManagedThread mT = build();
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
