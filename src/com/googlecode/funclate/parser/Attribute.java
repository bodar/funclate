package com.googlecode.funclate.parser;

import com.googlecode.funclate.Funclate;
import com.googlecode.funclate.Renderer;
import com.googlecode.totallylazy.Value;

import java.util.Map;

public class Attribute implements Value<String>, Renderer<Map<String, Object>> {
    private final String value;
    private final Funclate funclate;

    public Attribute(String value, Funclate funclate) {
        this.value = value;
        this.funclate = funclate;
    }

    public String value() {
        return value;
    }

    public String render(Map<String, Object> map) throws Exception {
        return funclate.render(map.get(value));
    }
}
