package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;

public interface Funclates extends Renderer<Object> {
    <T> Funclates add(String name, Predicate<? super T> predicate, Renderer<? super T> renderer);

    <T> Funclates add(String name, Predicate<? super T> predicate, Callable1<? super T, String> callable);

    <T> Funclates add(Predicate<? super T> predicate, Renderer<? super T> renderer);

    <T> Funclates add(Predicate<? super T> predicate, Callable1<? super T, String> callable);

    Renderer<Object> get(String name);
}
