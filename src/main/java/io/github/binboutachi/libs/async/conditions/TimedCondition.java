package io.github.binboutachi.libs.async.conditions;

import io.github.binboutachi.libs.LibInit;

public final class TimedCondition implements ExecuteCondition {
    private long delay = 0l;
    private long pauseStart = -1l;
    private long timestamp = 0l;

    public TimedCondition(Long d) {
        timestamp = System.currentTimeMillis();
        delay = d;
        if(delay < 0 || timestamp + delay < 0l)
            throw new IllegalArgumentException("delay is negative, or caused System.currentTimeMillis() + delay to become negative.");
    }
    

    @Override
    public boolean isReached() {
        if(isPaused()) {
            if(LibInit.DEBUG_ENABLED)
                LOGGER.debug("Paused, so TimedCondition cannot be reached.");
            return false;
        }
        if(LibInit.DEBUG_ENABLED)
            LOGGER.debug("isReached of TimedCondition: %b, current time: %d, timestamp: %d, delay: %d".formatted(System.currentTimeMillis() - timestamp >= delay,
                                                                                                            System.currentTimeMillis(),
                                                                                                            timestamp,
                                                                                                            delay));
        return System.currentTimeMillis() - timestamp >= delay;
    }

    @Override
    public void pause() {
        pauseStart = System.currentTimeMillis();
    }
    
    @Override
    public void unpauseImpl() {
        timestamp += System.currentTimeMillis() - pauseStart;
        pauseStart = -1;
    }

    @Override
    public boolean isPaused() {
        return !(pauseStart < 0l);
    }

    // @Override
    // public void setCondition(Long newVal) {
    //     delay = newVal;
    // }

    @Override
    public long timestamp() {
        return timestamp;
    }
    
    @Override
    public void refresh() {
        timestamp = System.currentTimeMillis();
    }
}
