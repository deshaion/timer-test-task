package com.demo.test;

import java.util.Arrays;

public class DelayedHeapImpl implements DelayedHeap {
    private ScheduledCommand[] heap = new ScheduledCommand[128];
    private int size = 0;

    @Override
    public void put(ScheduledCommand command) {
        growIfNeeded();
        heap[size++] = command;
        siftUp(size - 1);
    }

    private void growIfNeeded() {
        if (size == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
    }

    private void siftUp(int i) {
        while (heap[i].compareTo(heap[(i - 1) / 2]) < 0) {
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    private void siftDown(int i) {
        while (2 * i + 1 < size) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int j = left;
            if (right < size && heap[right].compareTo(heap[left]) < 0) {
                j = right;
            }
            if (heap[i].compareTo(heap[j]) <= 0) {
                break;
            }
            swap(i, j);
            i = j;
        }
    }

    private void swap(int i, int j) {
        ScheduledCommand t = heap[i];
        heap[i] = heap[j];
        heap[j] = t;
    }

    @Override
    public ScheduledCommand getEarliest() {
        if (isEmpty()) {
            return null;
        }
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public ScheduledCommand removeEarliest() {
        ScheduledCommand command = heap[0];

        heap[0] = heap[size - 1];
        size--;
        siftDown(0);

        return command;
    }
}
