package com.demo.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestTask implements Runnable {
    private final AtomicLong numberOfInvocations = new AtomicLong(0L);

    @Override
    public void run() {
        numberOfInvocations.getAndIncrement();
    }

    void times(long times) {
        assertThat("Number of invocations is incorrect", numberOfInvocations.get(), is(times));
    }

    TestTask timeout(long delay) {
        return timeout(delay, TimeUnit.MILLISECONDS);
    }

    TestTask timeout(long delay, TimeUnit unit) {
        final long timeout = TimeUnit.MILLISECONDS.convert(delay, unit);
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {}
        return this;
    }
}