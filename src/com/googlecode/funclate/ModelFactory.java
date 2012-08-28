package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;

import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface ModelFactory {
    Model create();

    Model create(Iterable<? extends Pair<String, Object>> values);

    Model create(Map<String, Object> values);

    Model create(String json);

    class methods {
        private methods() {
        }

        public static Model fromMap(ModelFactory factory, Map<String, Object> map) {
            return factory.create(Maps.pairs(map)).map(convert(factory));
        }

        private static Function1<Object, Object> convert(final ModelFactory factory) {
            return new Function1<Object, Object>() {
                public Object call(Object o) throws Exception {
                    return convert(factory, o);
                }
            };
        }

        private static Object convert(final ModelFactory factory, final Object value) {
            if (value instanceof Map) {
                return fromMap(factory, (Map<String, Object>) value);
            }
            if (value instanceof List) {
                return sequence((List<Object>) value).map(new Callable1<Object, Object>() {
                    public Object call(Object o) throws Exception {
                        return convert(factory, o);
                    }
                }).toList();
            }
            return value;
        }
    }
}
