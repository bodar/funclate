package com.googlecode.funclate.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Value;

import java.util.Map;

public class Text implements Value<String>, Callable1<Map<String, Object>, Object> {
    private final String value;

    public Text(String value) {
        this.value = value;
    }

    public Object call(Map<String, Object> map) throws Exception {
        return value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
