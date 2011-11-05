package com.googlecode.funclate.parser;

import com.googlecode.funclate.Funclate;
import com.googlecode.funclate.Renderer;

import java.util.Map;

public class TemplateCall implements Renderer<Map<String, Object>> {
    private final String name;
    private final Map<String, String> arguments;
    private final Funclate funclate;

    public TemplateCall(final String name, final Map<String, String> arguments, final Funclate funclate) {
        this.name = name;
        this.arguments = arguments;
        this.funclate = funclate;
    }

    public String name() {
        return name;
    }

    public Map<String, String> arguments() {
        return arguments;
    }

    public String render(Map<String, Object> map) throws Exception {
        return funclate.get(name).render(arguments);
    }
}
