package io.github.binboutachi.libs.async;

import java.util.function.Consumer;
import java.util.function.BiConsumer;

import io.github.binboutachi.libs.async.conditions.ExecuteCondition;
import io.github.binboutachi.libs.async.conditions.TautologicalCondition;
import io.github.binboutachi.libs.async.conditions.TimedCondition;

public final class ManagedThreadBuilder {
    private static final String MULTIPLE_EXCEPTION_HANDLERS_MSG = "At least two different variants of the overloaded function withExceptionHandler() was used. Only one is allowed per ManagedThread.";
    private static final String MULTIPLE_FUNCTIONS_MSG = "At least two different variants of the overloaded function withFunction() was used. Only one is allowed per ManagedThread.";
    private ExecuteCondition condition;
    private Runnable f;
    private Consumer<ManagedThread> c;
    private Consumer<Exception> onException;
    private BiConsumer<Exception, ManagedThread> onExceptionBi;
    public ManagedThreadBuilder() {}

    /**
     * Builds a {@code ManagedThread} based on
     * the state of this builder.
     * @return the {@code ManagedThread} instance
     * based on the state of this builder
     */
    public ManagedThread build() {
        if(f == null && c == null)
            throw new IllegalStateException("No function for the ManagedThread to run was specified.");
        ExecuteCondition execCondition = condition;
        if(condition == null)
            execCondition = new TautologicalCondition();
        return new ManagedThread(f, c, execCondition, onException, onExceptionBi);
    }
    /**
     * Convenience method to build and subsequently start
     * a {@code ManagedThread} from this builder.
     * @return the {@code ManagedThread} instance
     * based on the state of this builder
     */
    public ManagedThread buildAndStart() {
        ManagedThread mT = build();
        mT.start();
        return mT;
    }
    /**
     * <p>Resets this builder. </p>
     * More technically, all instance variables
     * are set to {@code null}, as is the
     * case when first instantiating an object
     * of a {@code ManagedThreadBuilder}.
     */
    public void reset() {
        f = null;
        condition = null;
        onException = null;
    }
    /**
     * Sets the given {@code Runnable} to be the
     * code that executes upon reaching this
     * {@code ManagedThread}'s {@code ExecuteCondition}.
     * @param func the code to run
     * @return this {@code ManagedThreadBuilder} instance
     * @throws IllegalStateException when another variant
     * of this function (such as {@link #withFunction(Consumer)})
     * was called beforehand
     */
    public ManagedThreadBuilder withFunction(Runnable func) {
        if(c != null)
            throw new IllegalStateException(MULTIPLE_FUNCTIONS_MSG);
        f = func;
        return this;
    }
    /**
     * Sets the given {@code Consumer} to be the
     * code that executes upon reaching this
     * {@code ManagedThread}'s {@code ExecuteCondition}.
     * This {@code Consumer} receives a reference to the
     * executing {@code ManagedThread} as its sole
     * parameter.
     * @param func the function that takes a reference to
     * the {@code ManagedThread} that will be executing
     * this function
     * @return this {@code ManagedThreadBuilder} instance
     * @throws IllegalStateException when another variant
     * of this function (such as {@link #withFunction(Runnable)})
     * was called beforehand
     */
    public ManagedThreadBuilder withFunction(Consumer<ManagedThread> func) {
        if(f != null)
            throw new IllegalStateException(MULTIPLE_FUNCTIONS_MSG);
        c = func;
        return this;
    }
    /**
     * Sets the {@code ExecuteCondition} to be one that
     * evaluates to {@code true} in {@code delayMillis}
     * milliseconds after starting the {@code ManagedThread}.
     * @param delayMillis the time to wait in milliseconds
     * until execution of the function
     * @return this {@code ManagedThreadBuilder} instance
     */
    public ManagedThreadBuilder withDelay(long delayMillis) {
        condition = new TimedCondition(delayMillis);
        return this;
    }
    /**
     * Appends a {@code Consumer} that is executed upon
     * encountering an exception in the {@code ManagedThread}.
     * @param onExc the exception handler
     * @return this {@code ManagedThreadBuilder} instance
     */
    public ManagedThreadBuilder withExceptionHandler(Consumer<Exception> onExc) {
        if(onExceptionBi != null)
            throw new IllegalStateException(MULTIPLE_EXCEPTION_HANDLERS_MSG);
        onException = onExc;
        return this;
    }
    /**
     * Appends a {@code BiConsumer} that is executed upon
     * encountering an exception in the {@code ManagedThread}.
     * It receives both a reference to the causing {@code Exception}
     * and a reference to the executing {@code ManagedThread}.
     * @param onExc the exception handler, also taking a reference
     * to the executing {@code ManagedThread}
     * @return this {@code ManagedThreadBuilder} instance
     */
    public ManagedThreadBuilder withExceptionHandler(BiConsumer<Exception, ManagedThread> onExc) {
        if(onException != null)
            throw new IllegalStateException(MULTIPLE_EXCEPTION_HANDLERS_MSG);
        onExceptionBi = onExc;
        return this;
    }
    // public <T> Builder withCondition(ExecuteCondition<T> c) {
    //     condition = c;
    //     return this;
    // }
}
