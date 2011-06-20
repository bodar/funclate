package com.googlecode.funclate.parser;

import com.googlecode.funclate.Funclates;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;

import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Template implements Callable1<Map<String, Object>, Object>{
    private final Sequence<Callable1<Map<String, Object>, Object>> objects;
    private Funclates funclates;

    public Template(Iterable<Callable1<Map<String, Object>, Object>> objects) {
        this.objects = sequence(objects);
    }

    public void funclates(Funclates funclates) {
        this.funclates = funclates;
    }

    public Object call(final Map<String, Object> map) throws Exception {
        return objects.map(new Callable1<Callable1<Map<String, Object>, Object>, Object>() {
            public Object call(Callable1<Map<String, Object>, Object> callable) throws Exception {
                return funclates.call(callable.call(map));
            }
        }).toString(Strings.EMPTY, Strings.EMPTY, Strings.EMPTY, Integer.MAX_VALUE);
    }
}
