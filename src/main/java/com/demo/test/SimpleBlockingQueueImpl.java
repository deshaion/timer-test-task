package com.demo.test;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleBlockingQueueImpl<T> implements SimpleBlockingQueue<T> {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Queue<T> queue = new ArrayDeque<>();

    @Override
    public void add(T value) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            queue.add(value);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T remove() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                }
            }
            return queue.remove();
        } finally {
            lock.unlock();
        }
    }
}
