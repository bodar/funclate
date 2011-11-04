package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Pair.pair;

public class BaseFunclates implements Funclates{
    public static final String NO_NAME = "";
    protected final Map<String, Renderers> funclates = new HashMap<String, Renderers>();

    public <T> Funclates add(Predicate<? super T> predicate, String format, Renderer<? super T> renderer) {
        get(format).add(predicate, renderer);
        return this;
    }

    public <T> Funclates add(Predicate<? super T> predicate, String format, Callable1<? super T, String> callable) {
        get(format).add(predicate, callable);
        return this;
    }

    public <T> Funclates add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        get(NO_NAME).add(predicate, renderer);
        return this;
    }

    public <T> Funclates add(Predicate<? super T> predicate, Callable1<? super T, String> renderer) {
        get(NO_NAME).add(predicate, renderer);
        return this;
    }

    public String call(String name, Object value) throws Exception {
        return funclates.get(name).render(value);
    }
    
    public String render(Object value) throws Exception {
        return get(NO_NAME).render(value);
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

    public Renderers get(String name) {
        if(!funclates.containsKey(normalise(name))) {
            funclates.put(normalise(name), new Renderers());
        }
        return funclates.get(normalise(name));
    }

    public static String normalise(String name) {
        return name.trim().toLowerCase();
    }


}
