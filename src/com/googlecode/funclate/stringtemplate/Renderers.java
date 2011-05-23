package com.googlecode.funclate.stringtemplate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import org.antlr.stringtemplate.AttributeRenderer;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Renderers implements AttributeRenderer{
    private final List<Pair<Predicate, Callable1>> pairs = new ArrayList<Pair<Predicate, Callable1>>();

    public String toString(Object value) {
        Object result = call(sequence(pairs).find(where(first(Predicate.class), (Predicate<? super Predicate>) Predicates.matches(value))).
                map(second(Callable1.class)).getOrElse(Callables.<Object>asString()), value);
        if(value instanceof String && result instanceof String){
            return (String) result;
        }
        return toString(result);
    }

    public String toString(Object value, String format) {
        throw new UnsupportedOperationException("We don't support format yet");
    }

    public <T, R> Renderers add(Predicate<? super T> predicate, Callable1<T, R> callable) {
        pairs.add(Pair.<Predicate, Callable1>pair(predicate, callable));
        return this;
    }
}
