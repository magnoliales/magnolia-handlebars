package com.magnoliales.handlebars.mapper;

/**
 * Wrapper object for delayed execution.
 * @param <T>
 */
public interface Delayed<T> {
    T get();
}
