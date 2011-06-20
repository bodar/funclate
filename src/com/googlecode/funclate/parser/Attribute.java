package com.googlecode.funclate.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Value;

import java.util.Map;

public class Attribute implements Value<String>, Callable1<Map<String, Object>, Object> {
    private final String value;

    public Attribute(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public Object call(Map<String, Object> map) throws Exception {
        return map.get(value);
    }
}
