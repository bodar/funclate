package com.googlecode.funclate;

import com.googlecode.totallylazy.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.googlecode.funclate.Model.functions.merge;
import static com.googlecode.funclate.Model.functions.mergeFlattenChildren;
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

            return map.map(new Callable1<Pair<String, String>, Pair<Sequence<String>, Pair<String, String>>>() {
                @Override
                public Pair<Sequence<String>, Pair<String, String>> call(Pair<String, String> keyValuePair) throws Exception {
                    Sequence<String> keys = Sequences.sequence(keyValuePair.first().split("\\."));
                    return Pair.pair(keys.init(), Pair.pair(keys.last(), keyValuePair.second()));
                }
            }).map(new Mapper<Pair<Sequence<String>, Pair<String, String>>, Model>() {
                @Override
                public Model call(Pair<Sequence<String>, Pair<String, String>> sequencePairPair) throws Exception {
                    Sequence<String> hierarchy = sequencePairPair.first();
                    Pair<String, String> keyAndValue = sequencePairPair.second();
                    Model previous = factory.create().add(keyAndValue.first(), keyAndValue.second());

                    for (String key : hierarchy.reverse()) {
                        previous = factory.create().add(key, previous);
                    }

                    return previous;
                }
            }).reduce(mergeFlattenChildren());
        }

        private Model createModel(ModelFactory factory, Model root, Model model, Sequence<String> hierarchy) {
            if (hierarchy.isEmpty())
                return root;
            String head = hierarchy.head();
            if (model.get(head) == null) {
                Model value = factory.create();
                Model newModel = model.add(head, value);
                return createModel(factory, root, value, hierarchy.tail());
            } else
                return createModel(factory, root, model.<Model>get(head), hierarchy.tail());
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
