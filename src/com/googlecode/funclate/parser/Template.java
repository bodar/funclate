package com.googlecode.funclate.parser;

import com.googlecode.funclate.Funclates;
import com.googlecode.funclate.Renderer;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;

import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Template implements Renderer<Pair<Map<String, Object>, Funclates>> {
    private final Sequence<Renderer<Pair<Map<String, Object>, Funclates>>> objects;

    public Template(Iterable<Renderer<Pair<Map<String, Object>, Funclates>>> objects) {
        this.objects = sequence(objects);
    }

    public String render(final Pair<Map<String, Object>, Funclates> pair) throws Exception {
        return objects.map(new Callable1<Renderer<Pair<Map<String, Object>, Funclates>>, Object>() {
            public Object call(Renderer<Pair<Map<String, Object>, Funclates>> renderer) throws Exception {
                return renderer.render(pair);
            }
        }).toString(Strings.EMPTY, Strings.EMPTY, Strings.EMPTY, Integer.MAX_VALUE);
    }
}
