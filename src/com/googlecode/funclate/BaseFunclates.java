package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;

import java.util.HashMap;
import java.util.Map;

public class BaseFunclates implements Funclates{
    public static final String NO_NAME = "";
    protected final Map<String, Renderers> funclates = new HashMap<String, Renderers>();
    private final Renderer<Object> parent;

    public BaseFunclates(Renderer<Object> parent) {
        this.parent = parent;
    }

    public BaseFunclates() {
        this.parent = asString();
    }

    private Renderer<Object> asString() {
        return new Renderer<Object>() {
            public String render(Object instance) throws Exception {
                return instance.toString();
            }
        };
    }

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

    public boolean contains(String name) {
        return funclates.containsKey(normalise(name));
    }

    protected Renderers renderersFor(String name) {
        String normalisedName = normalise(name);
        if(!contains(normalisedName)) {
            funclates.put(normalisedName, new Renderers(parent));
        }
        return funclates.get(normalisedName);
    }

    public static String normalise(String name) {
        return name.trim().toLowerCase();
    }


}
