package com.googlecode.funclate;

import com.googlecode.totallylazy.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface ModelFactory {
    Model create();

    Model create(Iterable<? extends Pair<String, ? extends Object>> values);

    Model create(Map<String, ? extends Object> values);

    Model create(String json);

    Model create(Properties properties);

    Iterable<?> toList(Iterable<?> map);

    class methods {
        public static Model fromMap(ModelFactory factory, Map<String, ? extends Object> map) {
            return factory.create(Maps.pairs(map)).map(convert(factory));
        }

        public static Model fromProperties(final ModelFactory factory, Properties properties) {
            Sequence<Pair<String, String>> map = Sequences.sequence(properties.entrySet()).map(Maps.entryToPair()).unsafeCast();

            return factory.create(map.map(new Callable1<Pair<String, String>, Pair<Sequence<String>, Pair<String, String>>>() {
                @Override
                public Pair<Sequence<String>, Pair<String, String>> call(Pair<String, String> keyValuePair) throws Exception {
                    Sequence<String> keys = Sequences.sequence(keyValuePair.first().split("\\."));
                    return Pair.pair(keys.init(), Pair.pair(keys.last(), keyValuePair.second()));
                }
            }).fold(new HashMap<String, Object>(), new Callable2<HashMap<String, Object>, Pair<Sequence<String>, Pair<String, String>>, HashMap<String, Object>>() {
                @Override
                public HashMap<String, Object> call(HashMap<String, Object> map, Pair<Sequence<String>, Pair<String, String>> hierarchyAndValue) throws Exception {
                    Sequence<String> hierarchy = hierarchyAndValue.first();
                    Pair<String, String> value = hierarchyAndValue.second();

                    HashMap<String, Object> next = map;
                    for (String key : hierarchy) {
                        if (!next.containsKey(key)) {
                            next.put(key, new HashMap<String, Object>());
                        }
                        next = (HashMap<String, Object>) next.get(key);
                    }

                    next.put(value.first(), value.second());
                    return map;
                }
            }));
        }

        private static Function1<Object, Object> convert(final ModelFactory factory) {
            return new Function1<Object, Object>() {
                public Object call(Object o) throws Exception {
                    return convert(factory, o);
                }
            };
        }

        private static Object convert(final ModelFactory factory, final Object value) {
            if (value instanceof Map) return fromMap(factory, (Map<String, Object>) value);
            if (value instanceof List)
                return factory.toList(sequence((List<Object>) value).map(new Callable1<Object, Object>() {
                    public Object call(Object o) throws Exception {
                        return convert(factory, o);
                    }
                }));
            return value;
        }
    }
}
