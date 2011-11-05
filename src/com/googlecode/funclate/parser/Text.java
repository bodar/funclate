package com.googlecode.funclate.parser;

import com.googlecode.funclate.Renderer;
import com.googlecode.totallylazy.Value;

import java.util.Map;

public class Text implements Value<String>, Renderer<Map<String, Object>> {
    private final String value;

    public Text(String value) {
        this.value = value;
    }


    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String render(Map<String, Object> map) throws Exception {
        return value;
    }
}
