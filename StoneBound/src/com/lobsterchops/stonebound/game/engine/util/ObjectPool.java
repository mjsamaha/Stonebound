package com.lobsterchops.stonebound.game.engine.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

public class ObjectPool<T> {

    private final Deque<T> pool = new ArrayDeque<>();
    private final Supplier<T> factory;

    public ObjectPool(Supplier<T> factory) {
        this.factory = factory;
    }

    /** Pre-warm the pool with {@code count} instances. */
    public void prewarm(int count) {
        for (int i = 0; i < count; i++) {
            pool.push(factory.get());
        }
    }

    public T obtain() {
        return pool.isEmpty() ? factory.get() : pool.pop();
    }

    public void free(T object) {
        pool.push(object);
    }

    public int size() {
        return pool.size();
    }
}
