package com.googlecode.funclate;

import com.googlecode.funclate.json.Json;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.funclate.json.Json.toJson;
import static com.googlecode.totallylazy.Sequences.sequence;

@SuppressWarnings("unchecked")
public class Model {
    private final Map<String, Object> values = new LinkedHashMap<String, Object>();

    public static Model model() {
        return new Model();
    }

    public <T> Model add(String key, T value) {
        if (!contains(key)) {
            values.put(key, value);
            return this;
        }
        List list = new ArrayList(getValues(key, value.getClass()));
        if (value instanceof List) {
            list.addAll((List)value);
        } else {
            list.add(value);
        }
        values.put(key, list);
        return this;
    }

    public <T> Model set(String name, T value) {
        values.put(name, value);
        return this;
    }

    public <T> T remove(String key, Class<T> aClass) {
        return remove(key);
    }

    public <T> T remove(String key) {
        return (T) values.remove(key);
    }

    public boolean contains(String key) {
        return values.containsKey(key);
    }

    public <T> T get(String key, Class<T> aClass) {
        return this.<T>get(key);
    }

    public <T> T get(String key) {
        T t = (T) values.get(key);
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
        if(value == null) {
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
        return (T) values.get(key);
    }

    public Set<Map.Entry<String, Object>> entries() {
        return values.entrySet();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            result.put(entry.getKey(), toValue(entry.getValue()));
        }
        return result;
    }

    public static Callable1<? super Model, Map<String, Object>> asMap() {
        return new Callable1<Model, Map<String, Object>>() {
            public Map<String, Object> call(Model model) throws Exception {
                return model.toMap();
            }
        };
    }

    private Object toValue(Object value) {
        if (value instanceof Model) {
            return ((Model) value).toMap();
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

    public static <T> Callable1<? super Model, T> value(final String key, final Class<T> aClass) {
        return new Callable1<Model, T>() {
            public T call(Model model) throws Exception {
                return model.get(key, aClass);
            }
        };
    }

    @Override
    public String toString() {
        return toJson(this.toMap());
    }

    @Override
    public final boolean equals(final Object o) {
        return o instanceof Model && myFields().equals(((Model) o).myFields());
    }

    @Override
    public final int hashCode() {
        return myFields().hashCode();
    }

    protected Sequence myFields() {
        return sequence(entries());
    }

    public static Model fromMap(Map<String, Object> map) {
        return sequence(map.entrySet()).fold(model(), new Callable2<Model, Map.Entry<String, Object>, Model>() {
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

    public static Model parse(String value) {
        Map<String, Object> map = Json.parse(value);
        return fromMap(map);
    }

    public Model copy() {
        return fromMap(toMap());
    }
}
