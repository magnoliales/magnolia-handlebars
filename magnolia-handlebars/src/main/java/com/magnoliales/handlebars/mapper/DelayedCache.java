package com.magnoliales.handlebars.mapper;

abstract class DelayedCache<T> implements Delayed<T> {

    private T value;

    public final T get() {
        if (value == null) {
            try {
                value = init();
            } catch (Exception e) {
                throw new RuntimeException("Cannot proceed with delayed execution", e);
            }
        }
        return value;
    }

    protected abstract T init() throws Exception;
}
