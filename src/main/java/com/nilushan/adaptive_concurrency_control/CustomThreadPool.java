package com.nilushan.adaptive_concurrency_control;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomThreadPool {

    private final ThreadPoolExecutor executor;

    /**
     * The constructor
     *
     * @param initialPoolSize size of thread pool
     */
    public CustomThreadPool(int initialPoolSize) {

        int KEEP_ALIVE_TIME = 100;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        executor = new ThreadPoolExecutor(initialPoolSize, initialPoolSize, KEEP_ALIVE_TIME, timeUnit,
                new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * Submits a task to the thread pool
     *
     * @param worker to be executed in the thread pool
     */
    public void submitTask(Runnable worker) {
        executor.execute(worker);
    }

    /**
     * Increments the pool size by n. No upper limit on the pool size
     */
    public void incrementPoolSizeBy(int n) {
        executor.setMaximumPoolSize(executor.getMaximumPoolSize() + n);
        executor.setCorePoolSize(executor.getCorePoolSize() + n);
    }

    /**
     * Decrement the pool size by n. Minimum allowed size is 1
     *
     * @param n the number to increment by
     */
    public void decrementPoolSizeBy(int n) {
        if (executor.getCorePoolSize() - n > 0 && executor.getMaximumPoolSize() - n > 0) {
            executor.setCorePoolSize(executor.getCorePoolSize() - n);
            executor.setMaximumPoolSize(executor.getMaximumPoolSize() - n);
        }
    }

    /**
     * Returns the size of the thread pool
     */
    public int getThreadPoolSize() {
        return executor.getPoolSize();

    }

    public void decrementPoolSizeTo(int n) {
        if (n > 0) {
            executor.setCorePoolSize(n);
            executor.setMaximumPoolSize(n);
        }
    }

    public void incrementPoolTo(int n) {
        if (n > executor.getCorePoolSize()) {
            executor.setMaximumPoolSize(n);
            executor.setCorePoolSize(n);
        }
        else if (n < executor.getCorePoolSize()) {
            decrementPoolSizeTo(n);

        }
    }
}
