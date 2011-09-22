package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Pair.pair;

public class BaseFunclates implements Funclates{
    protected final Map<String, Callable1> funclates = new HashMap<String, Callable1>();
    protected final Renderers renderers = new Renderers();

    public Funclates add(String name, Callable1 callable) {
        funclates.put(name, callable);
        return this;
    }

    public <T> Funclates add(Predicate<? super T> predicate, Renderer<T> renderer) {
        renderers.add(predicate, renderer);
        return this;
    }

    public <T> Funclates add(Predicate<? super T> predicate, Callable1<? super T, String> renderer) {
        renderers.add(predicate, renderer);
        return this;
    }

    public String call(String name, Object value) throws Exception {
        return String.valueOf(funclates.get(name).call(value));
    }
    
    public String render(Object value) throws Exception {
        return renderers.render(value);
    }

    protected Map convertToMap(Object value) {
        if (value instanceof Map) {
            return (Map) value;
        }
        if (value instanceof Model) {
            return ((Model) value).toMap();
        }
        return map(pair("value", value));
    }
}
