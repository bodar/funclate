package com.googlecode.funclate.parser;

import com.googlecode.funclate.Funclates;
import com.googlecode.funclate.Renderer;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Value;

import java.util.Map;

public class Attribute implements Value<String>, Renderer<Pair<Map<String, Object>, Funclates>> {
    private final String value;

    public Attribute(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String render(Pair<Map<String, Object>, Funclates> pair) throws Exception {
        return pair.second().render(pair.first().get(value));
    }
}
