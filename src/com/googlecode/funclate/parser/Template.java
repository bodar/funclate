package com.googlecode.funclate.parser;

import com.googlecode.funclate.Renderer;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;

import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Template implements Renderer<Map<String, Object>> {
    private final Sequence<Renderer<Map<String, Object>>> objects;

    public Template(Iterable<Renderer<Map<String, Object>>> objects) {
        this.objects = sequence(objects);
    }

    public String render(final Map<String, Object> map) throws Exception {
        return objects.map(functions.render(map)).toString(Strings.EMPTY);
    }

}
