package com.googlecode.funclate;

import com.googlecode.totallylazy.Mapper;

public interface Renderer<T> {
    String render(T instance) throws Exception;

    class functions {
        public static <T> Mapper<Renderer<T>, String> render(final T instance) {
            return new Mapper<Renderer<T>, String>() {
                public String call(Renderer<T> renderer) throws Exception {
                    return renderer.render(instance);
                }
            };
        }

    }
}
