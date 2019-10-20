package com.demo.test;

import java.util.concurrent.TimeUnit;

public class ScheduledCommand implements Comparable<ScheduledCommand> {
    private final Runnable command;
    private final long time;

    ScheduledCommand(Runnable command, long delay, TimeUnit unit) {
        this.command = command;

        //TODO check if it is possible to overflow Long.MAX_VALUE
        this.time = now() + unit.toNanos((delay < 0) ? 0 : delay);
    }

    public long getDelay() {
        return time - now();
    }

    public Runnable getCommand() {
        return command;
    }

    private long now() {
        return System.nanoTime();
    }

    @Override
    public int compareTo(ScheduledCommand other) {
        if (other == this) // compare zero if same object
            return 0;
        if (other != null) {
            long diff = time - other.time;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else
                return 0;
        } else {
            return -1;
        }
    }
}
