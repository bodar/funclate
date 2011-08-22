package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;

import java.util.HashMap;
import java.util.Map;

public class BaseFunclates implements Funclates{
    private final Map<String, Callable1> funclates = new HashMap<String, Callable1>();
    private final Renderers renderers = new Renderers();

    public Funclates add(String name, Callable1<?, String> callable) {
        funclates.put(name, callable);
        return this;
    }

    public <T> Funclates add(Predicate<? super T> predicate, Callable1<T, ?> callable) {
        renderers.add(predicate, callable);
        return this;
    }

    public String call(String name, Object value) throws Exception {
        return funclates.get(name).call(value).toString();
    }

    public String call(Object value) throws Exception {
        return renderers.call(value);
    }
}
