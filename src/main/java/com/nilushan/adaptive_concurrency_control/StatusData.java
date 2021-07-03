package com.nilushan.adaptive_concurrency_control;

import java.util.concurrent.atomic.AtomicInteger;

public class StatusData {
    private static final AtomicInteger counter = new AtomicInteger();

    public static void increaseConnectionCounter() {
        synchronized (counter){
            int newValue = counter.intValue() + 1;
            counter.set(newValue);
        }
    }

    public static void decreaseConnectionCounter() {
        synchronized (counter){
            int newValue = counter.intValue() - 1;
            counter.set(newValue);
        }
    }

    public static int getActiveConnectionCounter() {
        return counter.get();
    }
}
