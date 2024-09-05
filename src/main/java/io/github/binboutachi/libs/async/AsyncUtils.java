package io.github.binboutachi.libs.async;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class AsyncUtils {
    private AsyncUtils() {}
    private static ScheduledThreadPoolExecutor stpExecutor = new ScheduledThreadPoolExecutor(5);
    private static ArrayList<ScheduledFuture<?>> futures = new ArrayList<>(6);
    
    /**
     * Interpolates the value {@code start} linearly to {@code end} over
     * a duration of {@code durationSecs} in seconds, with timesteps of
     * 50 ms. A function can be provided that can act upon the interpolated
     * value, where {@code func} is the function executed for each step.
     * @param start start value
     * @param end end value
     * @param durationSecs timeframe during which the linear interpolation
     * should take place. Expected in seconds.
     * @param func {@code Consumer} that acts upon the interpolated value
     * at every interpolation step
     * @return the {@code CompletableFuture<?>} that represents
     * the asynchronous compution, which can be used to time the
     * completion of the interpolation. 
     */
    public static CompletableFuture<?> interpolateValueLinear(float start, float end, float durationSecs, Consumer<Float> func) {
        if(func == null) {
            return null;
        }
        final long fixedDelay = 50l; // 50 ms that is
        final float inc = (end - start) / ((durationSecs * 1000) / fixedDelay);
        final long startTime = System.currentTimeMillis();
        final int index = futures.size();
        final ScheduledFuture<?> task = stpExecutor.scheduleAtFixedRate(() -> {
            try {
                final float c = start + ((System.currentTimeMillis() - startTime) * (inc / fixedDelay));
                if((c - end) <= 0.0001f) {
                    futures.get(index).cancel(true);
                    return;
                }
                if(func != null)
                    func.accept(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0l, fixedDelay, TimeUnit.MILLISECONDS);
        futures.add(task);
        return CompletableFuture.runAsync(() -> {
            try {
                task.get();
            } catch (Exception e) {
                // a scheduledfuture is never expected to return a get() call,
                // thus we catch the exception of the cancelled scheduledfuture
                // which serves as a "standard" return
                return;
            }
        });
    }
}
