package com.googlecode.funclate.parser;

import com.googlecode.funclate.Funclates;
import com.googlecode.funclate.Renderer;
import com.googlecode.totallylazy.Pair;

import java.util.Map;

public class TemplateCall implements Renderer<Pair<Map<String, Object>, Funclates>> {
    private final String name;
    private final Map<String, String> arguments;

    public TemplateCall(final String name, final Map<String, String> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String name() {
        return name;
    }

    public Map<String, String> arguments() {
        return arguments;
    }

    public String render(Pair<Map<String, Object>, Funclates> instance) throws Exception {
        return instance.second().get(name).render(arguments);
    }
}
