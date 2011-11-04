package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Renderers implements Renderer<Object>{
    private final Deque<Pair<Predicate<Object>, Callable1<Object, String>>> pairs = new ArrayDeque<Pair<Predicate<Object>, Callable1<Object, String>>>();
    private Callable1<Object, String> parent;

    public Renderers() {
        parent = Callables.asString();
    }

    public Renderers(Renderer<Object> parent) {
        this.parent = callable(parent);
    }

    public Renderers parent(Renderer<Object> parent) {
        this.parent = callable(parent);
        return this;
    }

    public String render(Object value) throws Exception {
        Predicate<Predicate<Object>> matches = Predicates.matches(value);
        return sequence(pairs).find(where(Callables.<Predicate<Object>>first(), matches)).
                map(Callables.<Callable1<Object, String>>second()).
                getOrElse(parent).
                call(value);
    }

    public <T, R> Renderers add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        return add(predicate, callable(renderer));
    }

    @SuppressWarnings("unchecked")
    public <T, R> Renderers add(Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        pairs.addFirst(Pair.<Predicate<Object>, Callable1<Object, String>>pair((Predicate<Object>) predicate, (Callable1<Object, String>) callable));
        return this;
    }

    public static <T> Callable1<T, String> callable(final Renderer<T> renderer) {
        return new Callable1<T, String>() {
            public String call(T t) throws Exception {
                return renderer.render(t);
            }
        };
    }

}
