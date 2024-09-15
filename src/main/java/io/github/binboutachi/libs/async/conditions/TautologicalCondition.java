package io.github.binboutachi.libs.async.conditions;

public final class TautologicalCondition implements ExecuteCondition {

    @Override
    public boolean isReached() {
        return true;
    }

    @Override
    public void pause() {
        return;
    }

    @Override
    public void unpauseImpl() {
        return;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public long timestamp() {
        return 0l;
    }

    @Override
    public void refresh() {
        return;
    }
    
}
