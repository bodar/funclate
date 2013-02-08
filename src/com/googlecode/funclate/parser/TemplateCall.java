package com.googlecode.funclate.parser;

import com.googlecode.funclate.Funclate;
import com.googlecode.funclate.Renderer;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Maps;

import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Maps.pairs;

public class TemplateCall implements Renderer<Map<String, Object>> {
    private final String name;
    private final Map<String, Renderer<Map<String, Object>>> arguments;
    private final Funclate funclate;

    public TemplateCall(final String name, final Map<String, Renderer<Map<String, Object>>> arguments, final Funclate funclate) {
        this.name = name;
        this.arguments = arguments;
        this.funclate = funclate;
    }

    public String name() {
        return name;
    }

    public Map<String, Renderer<Map<String, Object>>> arguments() {
        return arguments;
    }

    public String render(Map<String, Object> context) throws Exception {
        return funclate.get(name).render(apply(arguments, context));
    }

    private Map<String, String> apply(Map<String, Renderer<Map<String, Object>>> arguments, Map<String, Object> context) {
        return map(pairs(arguments).map(Callables.<String, Renderer<Map<String, Object>>, String>second(functions.render(context))));
    }
}
