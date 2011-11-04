package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Predicate;

public interface Funclates extends Callable2<String, Object, String>, Renderer<Object> {
    <T> Funclates add(Predicate<? super T> predicate, String format, Renderer<? super T> renderer);

    <T> Funclates add(Predicate<? super T> predicate, String format, Callable1<? super T, String> callable);

    <T> Funclates add(Predicate<? super T> predicate, Renderer<? super T> renderer);

    <T> Funclates add(Predicate<? super T> predicate, Callable1<? super T, String> callable);
}
