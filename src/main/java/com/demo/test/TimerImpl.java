package com.demo.test;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/* List the known gaps and deficiencies of your implementation
- It's difficult to implement any solution for task which you know is already available in Java Core but I tried to find a solution which provides the best performance
- It's not guaranteed that all commands will be performed in the exact delay (But threadPool is used for better throughput), especially if tasks are time-consuming 
- If a huge number of commands are scheduled with small delay then there will be a delay before they will be performed because they have to be put to the heap.
- There is no ability to cancel Timer
- I didn't do anything for Garbage Collecting this Timer. It's maybe better to set Daemon for threads. I need to investigate this more. Or another option is to add fake object and add stopping the threads in fake object's finalize method. 
- I didn't add Unit tests for DaleyedHeapImpl and SimpleBlockingQueueImpl
- There is a standard analog in Java Core ScheduledThreadPoolExecutor
*/
public class TimerImpl implements Timer {
    private final SimpleBlockingQueue<Runnable> blockingQueue = new SimpleBlockingQueueImpl<>();
    private final DelayedHeap delayedHeap = new DelayedHeapImpl(); // TODO I can use PriorityQueue here (of course it means that I need change logic below because they have different interfaces)
    private final Thread mainLoop;
    private final ConcurrentLinkedQueue<ScheduledCommand> buffer = new ConcurrentLinkedQueue<>();
    
    TimerImpl() {
        int nThreads = Runtime.getRuntime().availableProcessors();

        Thread[] threadPool = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            threadPool[i] = new Thread(() -> {
                //TODO maybe cancelling need to be able here
                while (true) {
                    Runnable task = blockingQueue.remove();
                    task.run();
                }
            });
            threadPool[i].start();
        }

        mainLoop = new Thread(() -> {
            //TODO maybe cancelling need to be able here
            while (true) {
                if (buffer.isEmpty() && delayedHeap.isEmpty()) {
                    LockSupport.park(this);
                }
                while (!buffer.isEmpty()) {
                    ScheduledCommand command = buffer.remove();
                    if (command.getDelay() <= 0) {
                        blockingQueue.add(command.getCommand());
                    } else {
                        delayedHeap.put(command);
                    }
                }

                ScheduledCommand command = delayedHeap.getEarliest();
                if (command == null) {
                    continue;
                }
                long delay = command.getDelay();
                if (delay <= 0) {
                    command = delayedHeap.removeEarliest();
                    blockingQueue.add(command.getCommand());
                } else {
                    LockSupport.parkNanos(this, delay);
                }
            }
        });
        mainLoop.start();
    }

    @Override
    public void schedule(Runnable command, long delay, TimeUnit unit) {
        buffer.add(new ScheduledCommand(command, delay, unit));
        LockSupport.unpark(mainLoop);
    }
}
