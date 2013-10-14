package com.googlecode.funclate;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.collections.PersistentList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.googlecode.funclate.json.Json.toJson;
import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Sequences.sequence;

@SuppressWarnings("unchecked")
public class MutableModel extends AbstractModel {
    private final Map<String, Object> values;

    private MutableModel(Map<String, Object> values) {
        this.values = values;
    }

    static MutableModel model(Iterable<? extends Pair<String, ? extends Object>> values) {
        return new MutableModel(Maps.map(values));
    }

    public <T> Model add(String key, T rawValue) {
        Object value = lift(rawValue);

        if (!contains(key)) {
            values.put(key, value);
            return this;
        }
        List list = getValues(key, value.getClass());
        if (value instanceof List) {
            list.addAll((List) value);
        } else {
            list.add(value);
        }
        values.put(key, list);
        return this;
    }

    private <T> Object lift(T value) {
        if (value instanceof Sequence) return ((Sequence) value).toList();
        if (value instanceof PersistentList) return ((PersistentList) value).toMutableList();
        return value;
    }

    public <T> Model set(String name, T value) {
        values.put(name, value);
        return this;
    }

    public <T> Pair<Model, Option<T>> remove(String key, Class<T> aClass) {
        return remove(key);
    }

    public <T> Pair<Model, Option<T>> remove(String key) {
        return Pair.<Model, Option<T>>pair(this, Option.<T>option((T) values.remove(key)));
    }

    public Model map(Callable1<? super Object, ?> callable) {
        return new MutableModel(Maps.mapValues(values, callable));
    }

    @Override
    public Model merge(Model other) {
        return Model.methods.merge(this, other);
    }

    public boolean contains(String key) {
        return values.containsKey(key);
    }

    public <T> T get(String key, Class<T> aClass) {
        return this.get(key);
    }

    public <T> T get(String key) {
        T t = (T) values.get(key);
        if (t instanceof List) {
            return (T) ((List) t).get(0);
        }
        return t;
    }

    @Override
    public <T> Option<T> getOption(String key, Class<T> aClass) {
        return getOption(key);
    }

    @Override
    public <T> Option<T> getOption(String key) {
        return option(this.<T>get(key));
    }

    public <T> List<T> getValues(String key, Class<T> aClass) {
        return this.getValues(key);
    }

    public <T> List<T> getValues(String key) {
        final Object value = getObject(key);
        if (value == null) return new ArrayList<T>();
        if (value instanceof List) return new ArrayList<T>((List) value);
        return new ArrayList() {{ add(value); }};
    }

    private <T> T getObject(String key) {
        return (T) values.get(key);
    }

    public Set<Map.Entry<String, Object>> entries() {
        return values.entrySet();
    }

    @Override
    public Iterable<Pair<String, Object>> pairs() {
        return Maps.pairs(values);
    }

    public Map<String, Object> toMap() {
        return Model.methods.toMap(this);
    }

    @Override
    public String toString() {
        return toJson(this.toMap());
    }

    @Override
    public final boolean equals(final Object o) {
        return o instanceof MutableModel && myFields().equals(((MutableModel) o).myFields());
    }

    @Override
    public final int hashCode() {
        return myFields().hashCode();
    }

    protected Sequence myFields() {
        return sequence(entries());
    }

    public Model copy() {
        return Model.mutable.model(toMap());
    }
}
