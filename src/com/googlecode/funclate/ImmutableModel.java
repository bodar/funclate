package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.collections.ImmutableList;
import com.googlecode.totallylazy.collections.ImmutableMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.funclate.Model.immutable.toModel;
import static com.googlecode.funclate.json.Json.toJson;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.collections.ImmutableList.constructors.empty;
import static com.googlecode.totallylazy.collections.ImmutableList.constructors.list;
import static com.googlecode.totallylazy.collections.ImmutableList.constructors.reverse;
import static com.googlecode.totallylazy.collections.ImmutableSortedMap.constructors.sortedMap;

public class ImmutableModel implements Model {
    private final ImmutableMap<String, Object> values;

    private ImmutableModel(ImmutableMap<String, Object> values) {
        this.values = values;
    }

    static ImmutableModel model(Iterable<? extends Pair<String, Object>> values) {
        return new ImmutableModel(sortedMap(values));
    }

    public <T> Model add(String key, T rawValue) {
        Object value = lift(rawValue);

        if (!contains(key)) return new ImmutableModel(values.put(key, value));

        ImmutableList<T> existingValues = values(key);
        if (value instanceof ImmutableList) {
            existingValues = Unchecked.<ImmutableList<T>>cast(value).joinTo(existingValues);
        } else {
            existingValues = existingValues.cons(Unchecked.<T>cast(value));
        }
        return new ImmutableModel(values.put(key, existingValues));
    }

    private <T> Object lift(T value) {
        if (value instanceof List) return listToImmutableList(value);
        if (value instanceof Sequence) return Unchecked.<Sequence<T>>cast(value).toImmutableList();
        return value;
    }

    private <T> ImmutableList<T> listToImmutableList(Object newValue) {
        return reverse(Unchecked.<List<T>>cast(newValue));
    }

    public <T> T get(String key, Class<T> aClass) {
        return this.get(key);
    }

    public <T> T get(String key) {
        T t = this.<T>object(key).getOrNull();
        if (t instanceof ImmutableList) return (sequence(Unchecked.<ImmutableList<T>>cast(t))).last();
        return t;
    }

    public <T> List<T> getValues(String key, Class<T> aClass) {
        return this.getValues(key);
    }

    public <T> List<T> getValues(String key) {
        return this.<T>values(key).toSequence().reverse().toList();
    }

    public <T> ImmutableList<T> values(String key) {
        Option<T> value = object(key);
        if (value.isEmpty()) return empty();
        final T t = value.get();
        if (t instanceof ImmutableList) return cast(t);
        return list(t);
    }

    private <T> Option<T> object(String key) {
        return cast(values.get(key));
    }

    public <T> Model set(String name, T value) {
        return new ImmutableModel(values.put(name, value));
    }

    public <T> Pair<Model, Option<T>> remove(String key, Class<T> aClass) {
        return remove(key);
    }

    public <T> Pair<Model, Option<T>> remove(String key) {
        return ImmutableMap.methods.<String, T, ImmutableMap>remove(values, key).map(toModel());
    }

    public ImmutableModel map(Callable1<? super Object, ?> callable) {
        return new ImmutableModel(values.map(callable));
    }

    public boolean contains(String key) {
        return values.contains(key);
    }

    public Model copy() {
        return this;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        for (Pair<String, Object> entry : values) {
            result.put(entry.first(), toValue(entry.second()));
        }
        return result;
    }

    private Object toValue(Object value) {
        if (value instanceof ImmutableModel) {
            return ((ImmutableModel) value).toMap();
        }
        if (value instanceof ImmutableList) {
            return sequence(Unchecked.<ImmutableList<Object>>cast(value)).map(toValue()).reverse().toList();
        }
        return value;
    }

    private Callable1<Object, Object> toValue() {
        return new Callable1<Object, Object>() {
            public Object call(Object o) throws Exception {
                return toValue(o);
            }
        };
    }

    public Set<Map.Entry<String, Object>> entries() {
        return toMap().entrySet();
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
        return sequence(values);
    }
}