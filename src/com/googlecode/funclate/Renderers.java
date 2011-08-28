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
    private final List<Pair<Predicate, Renderer>> pairs = new ArrayList<Pair<Predicate, Renderer>>();

    @SuppressWarnings("unchecked")
    public String render(Object value) throws Exception {
        return sequence(pairs).find(where(first(Predicate.class), (Predicate<? super Predicate>) Predicates.matches(value))).
                map(second(Renderer.class)).
                getOrElse(asString()).
                render(value);
    }

    public <T, R> Renderers add(Predicate<? super T> predicate, Renderer<T> callable) {
        pairs.add(Pair.<Predicate, Renderer>pair(predicate, callable));
        return this;
    }

    public static Renderer<Object> asString() {
        return new Renderer<Object>() {
            public String render(Object instance) {
                return String.valueOf(instance);
            }
        };
    }

    public static <T> Renderer<T> renderer(final Callable1<? super T, ?> callable){
        return new Renderer<T>() {
            public String render(T instance) throws Exception {
                return String.valueOf(callable.call(instance));
            }
        };
    }
}
