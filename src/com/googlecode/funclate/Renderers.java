package com.googlecode.funclate;

import com.googlecode.totallylazy.*;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Renderers implements Callable1<Object, String>{
    private final List<Pair<Predicate, Callable1>> pairs = new ArrayList<Pair<Predicate, Callable1>>();

    public String call(Object value) {
        Object result = Callers.call(sequence(pairs).find(where(first(Predicate.class), (Predicate<? super Predicate>) Predicates.matches(value))).
                map(second(Callable1.class)).getOrElse(Callables.<Object>asString()), value);
        if(value instanceof String && result instanceof String){
            return (String) result;
        }
        return call(result);
    }

    public <T, R> Renderers add(Predicate<? super T> predicate, Callable1<T, R> callable) {
        pairs.add(Pair.<Predicate, Callable1>pair(predicate, callable));
        return this;
    }
}
