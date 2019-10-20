package com.demo.test;

public interface SimpleBlockingQueue<T> {
    void add(T value);
    T remove();
}
