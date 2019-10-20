package com.demo.test;

public interface DelayedHeap {
    void put(ScheduledCommand command);

    ScheduledCommand getEarliest();

    ScheduledCommand removeEarliest();

    boolean isEmpty();
}
