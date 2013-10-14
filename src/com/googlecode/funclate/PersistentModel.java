package com.googlecode.funclate;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.collections.PersistentList;
import com.googlecode.totallylazy.collections.PersistentMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.googlecode.funclate.Model.persistent.toModel;
import static com.googlecode.funclate.json.Json.toJson;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.collections.ListMap.listMap;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.*;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors.sortedMap;

public class PersistentModel extends AbstractModel {
    private final PersistentMap<String, Object> values;

    private PersistentModel(PersistentMap<String, Object> values) {
        this.values = values;
    }

    static PersistentModel model(Iterable<? extends Pair<String, ? extends Object>> values) {
        return new PersistentModel(sortedMap(Unchecked.<Iterable<Pair<String, Object>>>cast(values)));
    }

    public <T> Model add(String key, T rawValue) {
        Object value = lift(rawValue);

        if (!contains(key)) return new PersistentModel(values.insert(key, value));

        PersistentList<T> existingValues = values(key);
        if (value instanceof PersistentList) {
            existingValues = Unchecked.<PersistentList<T>>cast(value).joinTo(existingValues);
        } else {
            existingValues = existingValues.cons(Unchecked.<T>cast(value));
        }
        return new PersistentModel(values.insert(key, existingValues));
    }

    private Object lift(Object value) {
        if (value instanceof PersistentList) return value;
        if (value instanceof List) return reverse(Unchecked.<List<Object>>cast(value));
        if (value instanceof Sequence) return reverse(Unchecked.<Sequence<Object>>cast(value));
        return value;
    }

    public <T> T get(String key, Class<T> aClass) {
        return this.get(key);
    }

    public <T> T get(String key) {
        T t = this.<T>getOption(key).getOrNull();
        if (t instanceof PersistentList) return (sequence(Unchecked.<PersistentList<T>>cast(t))).last();
        return t;
    }

    @Override
    public <T> Option<T> getOption(String key, Class<T> aClass) {
        return getOption(key);
    }

    @Override
    public <T> Option<T> getOption(String key) {
        return cast(values.lookup(key));
    }

    public <T> List<T> getValues(String key, Class<T> aClass) {
        return this.getValues(key);
    }

    public <T> List<T> getValues(String key) {
        return this.<T>values(key).toSequence().reverse().toList();
    }

    public <T> PersistentList<T> values(String key) {
        Option<T> value = getOption(key);
        if (value.isEmpty()) return empty();
        final T t = value.get();
        if (t instanceof PersistentList) return cast(t);
        return list(t);
    }

    public <T> Model set(String name, T value) {
        return new PersistentModel(values.insert(name, value));
    }

    public <T> Pair<Model, Option<T>> remove(String key, Class<T> aClass) {
        return remove(key);
    }

    public <T> Pair<Model, Option<T>> remove(String key) {
        return PersistentMap.methods.<String, T, PersistentMap>remove(values, key).map(toModel);
    }

    public PersistentModel map(Callable1<? super Object, ?> callable) {
        return new PersistentModel(values.map(callable));
    }

    @Override
    public Model merge(Model other) {
        return Model.methods.merge(this, other);
    }

    public boolean contains(String key) {
        return values.contains(key);
    }

    public Model copy() {
        return this;
    }

    public Map<String, Object> toMap() {
        return Model.methods.toMap(this);
    }

    public Set<Map.Entry<String, Object>> entries() {
        return listMap(pairs()).entrySet();
    }

    @Override
    public Iterable<Pair<String, Object>> pairs() {
        return values.map(reverseIfNeeded());
    }

    @Override
    public String toString() {
        return toJson(this.toMap());
    }

    @Override
    public final boolean equals(final Object o) {
        return o instanceof PersistentModel && myFields().equals(((PersistentModel) o).myFields());
    }

    @Override
    public final int hashCode() {
        return myFields().hashCode();
    }

    protected Sequence myFields() {
        return sequence(values);
    }

    private Mapper<Object, Object> reverseIfNeeded() {
        return new Mapper<Object, Object>() {
            @Override
            public Object call(Object value) throws Exception {
                if (value instanceof PersistentList) {
                    return Unchecked.<PersistentList>cast(value).reverse().toMutableList();
                }
                return value;
            }
        };
    }
}