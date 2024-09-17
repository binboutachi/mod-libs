package io.github.binboutachi.libs.async;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.github.binboutachi.libs.LibInit;

public final class AsyncUtils {
    private AsyncUtils() {}
    private static ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
    private static ArrayList<ScheduledFuture<?>> futures = new ArrayList<>(6);
    private static int INTERPOLATE_DURATION_LENIENCY = 1000;
    
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
        // final int index = futures.size();
        final CompletableFuture<?> task = CompletableFuture.runAsync(() -> {
            while(System.currentTimeMillis() - (startTime + INTERPOLATE_DURATION_LENIENCY) <= durationSecs * 1000) {
                try {
                    final float c = start + ((System.currentTimeMillis() - startTime) * (inc / fixedDelay));
                    if((c - end) <= 0.0001f) {
                        if(func != null)
                            func.accept(end);
                        return;
                    }
                    if(func != null)
                        func.accept(c);
                    Thread.sleep(fixedDelay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(func != null)
                func.accept(end);
        }, virtualExecutor);
        // futures.add(task);
        return task;
    }
    public static CompletableFuture<?> interpolateRGBALinear(int start, int end, float durationMillis, Consumer<Integer> func) {
        if(func == null) {
            return null;
        }
        final long fixedDelay = 50l; // 50 ms that is
        final int s_a = 0xFF & (start >> 24);
        final int s_r = 0xFF & (start >> 16);
        final int s_g = 0xFF & (start >> 8);
        final int s_b = 0xFF & (start);
        final int e_a = 0xFF & (end   >> 24);
        final int e_r = 0xFF & (end   >> 16) ;
        final int e_g = 0xFF & (end   >> 8);
        final int e_b = 0xFF & (end);
        final float inc_a = (e_a - s_a) / (durationMillis / fixedDelay);
        final float inc_r = (e_r - s_r) / (durationMillis / fixedDelay);
        final float inc_g = (e_g - s_g) / (durationMillis / fixedDelay);
        final float inc_b = (e_b - s_b) / (durationMillis / fixedDelay);
        final long startTime = System.currentTimeMillis();
        // final int index = futures.size();
        final CompletableFuture<?> task = CompletableFuture.runAsync(() -> {
            while(System.currentTimeMillis() - (startTime + INTERPOLATE_DURATION_LENIENCY) <= durationMillis) {
                try {
                    final int c_a = Math.round(s_a + ((System.currentTimeMillis() - startTime) * (inc_a / fixedDelay)));
                    final int c_r = Math.round(s_r + ((System.currentTimeMillis() - startTime) * (inc_r / fixedDelay)));
                    final int c_g = Math.round(s_g + ((System.currentTimeMillis() - startTime) * (inc_g / fixedDelay)));
                    final int c_b = Math.round(s_b + ((System.currentTimeMillis() - startTime) * (inc_b / fixedDelay)));
                    int cumulated = (c_a << 24) + (c_r << 16) + (c_g << 8) + c_b;
                    if(Math.abs(c_a - e_a) <= 0.0001f) { // since each is going to reach the end at the same time, the discriminator doesnt matter
                        if(func != null)
                            func.accept(end);
                        return;
                    }
                    if(func != null)
                        func.accept(cumulated);
                    Thread.sleep(fixedDelay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(func != null)
                func.accept(end);
        }, virtualExecutor);
        // futures.add(task);
        return task;
    }
}
