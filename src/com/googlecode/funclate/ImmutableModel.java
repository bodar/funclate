package com.googlecode.funclate;

import com.googlecode.funclate.json.Json;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.collections.ImmutableMap;
import com.googlecode.totallylazy.collections.ImmutableSortedMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.funclate.json.Json.toJson;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.sequence;

public class ImmutableModel implements Model {
    private final ImmutableMap<String, Object> values;

    private ImmutableModel(ImmutableMap<String, Object> values) {
        this.values = values;
    }

    static ImmutableModel model(Iterable<? extends Pair<String, Object>> values) {
        return new ImmutableModel(ImmutableSortedMap.constructors.sortedMap(values));
    }

    public <T> Model add(String key, T value) {
        if (!contains(key)) {
            return new ImmutableModel(values.put(key, value));
        }
        List list = new ArrayList(getValues(key, value.getClass()));
        if (value instanceof List) {
            list.addAll((List) value);
        } else {
            list.add(value);
        }
        return new ImmutableModel(values.put(key, list));
    }

    public <T> T get(String key, Class<T> aClass) {
        return this.<T>get(key);
    }

    public <T> T get(String key) {
        T t = (T) values.get(key).getOrNull();
        if (t instanceof List) {
            return (T) ((List) t).get(0);
        }
        return t;
    }

    public <T> List<T> getValues(String key, Class<T> aClass) {
        return this.<T>getValues(key);
    }

    public <T> List<T> getValues(String key) {
        final Object value = getObject(key);
        if (value == null) {
            return Collections.emptyList();
        }
        if (value instanceof List) {
            return (List) value;
        }
        return new ArrayList() {{
            add(value);
        }};
    }

    public <T> T getObject(String key) {
        return (T) values.get(key).getOrNull();
    }

    public <T> Model set(String name, T value) {
        return new ImmutableModel(values.put(name, value));
    }

    public <T> Pair<Model, T> remove(String key, Class<T> aClass) {
        return remove(key);
    }

    public <T> Pair<Model, T> remove(String key) {
        return Pair.<Model, T>pair(new ImmutableModel(values.remove(key)), this.<T>get(key));
    }

    public boolean contains(String key) {
        return !values.filterKeys(is(key)).isEmpty();
    }

    public Model copy() {
        return fromMap(toMap());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        for (Map.Entry<String, Object> entry : entries()) {
            result.put(entry.getKey(), toValue(entry.getValue()));
        }
        return result;
    }

    private Object toValue(Object value) {
        if (value instanceof ImmutableModel) {
            return ((ImmutableModel) value).toMap();
        }
        if (value instanceof List) {
            return Sequences.sequence((List) value).map(toValue()).toList();
        }
        return value;
    }

    private Callable1 toValue() {
        return new Callable1() {
            public Object call(Object o) throws Exception {
                return toValue(o);
            }
        };
    }

    public Set<Map.Entry<String, Object>> entries() {
        return values.toMap().entrySet();
    }

    @Override
    public String toString() {
        return toJson(this.toMap());
    }

    @Override
    public final boolean equals(final Object o) {
        return o instanceof ImmutableModel && myFields().equals(((ImmutableModel) o).myFields());
    }

    @Override
    public final int hashCode() {
        return myFields().hashCode();
    }

    protected Sequence myFields() {
        return sequence(entries());
    }

    public static Model fromMap(Map<String, Object> map) {
        return sequence(map.entrySet()).fold(immutable.model(), new Function2<Model, Map.Entry<String, Object>, Model>() {
            public Model call(Model model, Map.Entry<String, Object> entry) throws Exception {
                return model.add(entry.getKey(), convert(entry.getValue()));
            }
        });
    }

    private static Object convert(final Object value) {
        if (value instanceof Map) {
            return fromMap((Map<String, Object>) value);
        }
        if (value instanceof List) {
            return sequence((List<Object>) value).map(new Callable1<Object, Object>() {
                public Object call(Object o) throws Exception {
                    return convert(o);
                }
            }).toList();
        }
        return value;
    }

    public static Model parse(String json) {
        return fromMap(Json.parse(json));
    }
}