package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Predicate;

public interface Funclates extends Callable2<String, Object, String>, Callable1<Object, String> {
    Funclates add(String name, Callable1<?,String> callable);

    <T> Funclates add(Predicate<? super T> predicate, Callable1<T,?> encode);
}
