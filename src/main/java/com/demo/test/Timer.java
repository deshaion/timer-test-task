package com.demo.test;

import java.util.concurrent.TimeUnit;

public interface Timer {
    /**
     * Creates and executes a one-shot action that becomes enabled
     * after the given delay.

     * @param command the task to execute
     * @param delay the time from now to delay execution
     * @param unit the time unit of the delay parameter
     **/
    void schedule(Runnable command, long delay, TimeUnit unit);
}
