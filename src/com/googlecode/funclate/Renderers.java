package com.googlecode.funclate;

import com.googlecode.totallylazy.*;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Renderers implements Renderer<Object>{
    private final List<Pair<Predicate, Callable1<Object, String>>> pairs = new ArrayList<Pair<Predicate, Callable1<Object, String>>>();

    @SuppressWarnings("unchecked")
    public String render(Object value) throws Exception {
        return sequence(pairs).find(where(first(Predicate.class), (Predicate<? super Predicate>) Predicates.matches(value))).
                map(Callables.<Callable1<Object, String>>second()).
                getOrElse(Callables.asString()).
                call(value);
    }

    public <T, R> Renderers add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        return add(predicate, callable(renderer));
    }

    public <T, R> Renderers add(Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        pairs.add(Pair.<Predicate, Callable1<Object, String>>pair(predicate, (Callable1<Object, String>) callable));
        return this;
    }

    public static <T> Callable1<? super T, String> callable(final Renderer<T> renderer) {
        return new Callable1<T, String>() {
            public String call(T t) throws Exception {
                return renderer.render(t);
            }
        };
    }

}
