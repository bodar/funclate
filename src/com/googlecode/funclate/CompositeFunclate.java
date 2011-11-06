package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.funclate.RendererContainer.methods.noParent;

public class CompositeFunclate implements Funclate {
    public static final String NO_NAME = "";
    protected final Map<String, MatchingRenderer> funclates = new HashMap<String, MatchingRenderer>();
    private final RendererContainer parent;

    public CompositeFunclate(RendererContainer parent) {
        this.parent = parent;
    }

    public CompositeFunclate() {
        this(noParent());
    }

    public <T> Funclate add(String name, Predicate<? super T> predicate, Renderer<? super T> renderer) {
        renderersFor(name).add(predicate, renderer);
        return this;
    }

    public <T> Funclate add(String name, Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        renderersFor(name).add(predicate, callable);
        return this;
    }

    public <T> Funclate add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        renderersFor(NO_NAME).add(predicate, renderer);
        return this;
    }

    public <T> Funclate add(Predicate<? super T> predicate, Callable1<? super T, String> renderer) {
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

    protected MatchingRenderer renderersFor(String name) {
        String normalisedName = normalise(name);
        if(!contains(normalisedName)) {
            create(normalisedName);
        }
        return funclates.get(normalisedName);
    }

    protected void create(String normalisedName) {
        funclates.put(normalisedName, new MatchingRenderer(parent.get(normalisedName)));
    }

    public static String normalise(String name) {
        return name.trim().toLowerCase();
    }


}
