package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;

import java.util.HashMap;
import java.util.Map;

public class BaseFunclates implements Funclates{
    public static final String NO_NAME = "";
    protected final Map<String, Renderers> funclates = new HashMap<String, Renderers>();

    public <T> Funclates add(String name, Predicate<? super T> predicate, Renderer<? super T> renderer) {
        renderersFor(name).add(predicate, renderer);
        return this;
    }

    public <T> Funclates add(String name, Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        renderersFor(name).add(predicate, callable);
        return this;
    }

    public <T> Funclates add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        renderersFor(NO_NAME).add(predicate, renderer);
        return this;
    }

    public <T> Funclates add(Predicate<? super T> predicate, Callable1<? super T, String> renderer) {
        renderersFor(NO_NAME).add(predicate, renderer);
        return this;
    }

    public String render(Object value) throws Exception {
        return get(NO_NAME).render(value);
    }

    public Renderer<Object> get(String name) {
        return renderersFor(name);
    }

    protected Renderers renderersFor(String name) {
        String normalisedName = normalise(name);
        if(!funclates.containsKey(normalisedName)) {
            funclates.put(normalisedName, new Renderers());
        }
        return funclates.get(normalisedName);
    }

    public static String normalise(String name) {
        return name.trim().toLowerCase();
    }


}
