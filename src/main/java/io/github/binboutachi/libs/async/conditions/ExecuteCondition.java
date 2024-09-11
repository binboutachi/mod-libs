package io.github.binboutachi.libs.async.conditions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An interface modelling an execute condition for a
 * {@code ManagedThread} instance, which will evaluate
 * to {@code true} sometime in the future and can,
 * depending on the type of condition implemented, be
 * paused to delay its evaluation.
 */
public interface ExecuteCondition {
    final Logger LOGGER = LogManager.getLogger(ExecuteCondition.class);
    // protected T condition;
    // public ExecuteCondition(T initial) {
    //     condition = initial;
    // }
    /**
     * The result of this function determines whether
     * this {@code ExecuteCondition} evaluates to
     * true. Once this method first returns {@code true},
     * the result must stay {@code true} for the
     * rest of the {@code ExecuteCondition}'s lifetime.
     * @return whether the {@code ExecuteCondition}
     * is reached
     */
    public boolean isReached();
    /**
     * Pauses this {@code ExecuteCondition}, and
     * may have no effect on the result of
     * {@link #isReached()}, depending on the type
     * of condition modelled.
     */
    public void pause();
    /**
     * Unpauses this {@code ExecuteCondition} if
     * and only if it is currently paused as
     * returned by {@link #isPaused()}. Like
     * {@link #pause()}, an invokation of this
     * method may yield no results depending on
     * the type of condition modelled. This
     * method can be overridden but it is
     * recommended to instead override
     * {@link #unpauseImpl()}.
     */
    public default void unpause() {
        if(!isPaused())
            return;
        unpauseImpl();
    }
    /**
     * The implementation of the {@link #unpause()}
     * method, which is to only perform the unpausing
     * depending on possibly condition-specific
     * circumstances. The default implementation of
     * {@link #unpause()} already prevents unpausing
     * when the condition is not currently paused, as
     * returned by {@link #isPaused()}.
     */
    void unpauseImpl();
    /**
     * Returns whether this {@code ExecuteCondition}
     * is currently paused. It is entirely up to
     * the implementation whether this method can
     * ever evaluate to {@code true}, as is dependent
     * on the functionality of {@link #pause()} and
     * {@link #unpause()}.
     * @return whether this {@code ExecuteCondition}
     * is currently paused. if the type of condition
     * modelled does not support pausing, this method
     * shall always return {@code false}.
     */
    public boolean isPaused();
    // public void setCondition(T newVal);
    /**
     * The timestamp at which this {@code ExecuteCondition}
     * was instantiated. May have no use depending
     * on the type of condition modelled.
     * @return the timestamp of the creation of
     * this {@code ExecuteCondition} instance
     */
    public long timestamp();
    /**
     * If the creation of this {@code ExecuteCondition}
     * is sufficiently long ago, this method refreshes
     * the condition to behave more expectedly. Might
     * have no effect depending on the underlying
     * condition type.
     */
    public void refresh();
}
