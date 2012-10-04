package com.googlecode.funclate;

public interface Renderer<T> {
    String render(T instance) throws Exception;
}
