package io.github.binboutachi.libs.async.conditions;

public interface ExecuteCondition {
    // protected T condition;
    // public ExecuteCondition(T initial) {
    //     condition = initial;
    // }
    public boolean isReached();
    public void pause();
    public void unpause();
    public boolean isPaused();
    // public void setCondition(T newVal);
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
