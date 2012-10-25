package com.googlecode.funclate;

import com.googlecode.funclate.json.Json;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.collections.ImmutableList;
import com.googlecode.totallylazy.collections.ImmutableMap;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface Model {
    <T> Model add(String key, T value);

    <T> Model set(String name, T value);

    <T> T get(String key, Class<T> aClass);

    <T> T get(String key);

    <T> Option<T> getOption(String key, Class<T> aClass);

    <T> Option<T> getOption(String key);

    <T> List<T> getValues(String key, Class<T> aClass);

    <T> List<T> getValues(String key);

    boolean contains(String key);

    Model copy();

    Map<String, Object> toMap();

    Set<Map.Entry<String, Object>> entries();

    <T> Pair<Model, Option<T>> remove(String key, Class<T> aClass);

    <T> Pair<Model, Option<T>> remove(String key);

    Model map(Callable1<? super Object, ?> callable);


    enum mutable implements ModelFactory {
        instance;

        public static Model model() {
            return instance.create();
        }

        public static Model model(Iterable<? extends Pair<String, Object>> values) {
            return instance.create(values);
        }

        public static Model model(Map<String, Object> values) {
            return instance.create(values);
        }

        public static Model parse(String json) {
            return instance.create(json);
        }

        public Model create() {
            return create(Sequences.<Pair<String, Object>>empty());
        }

        public Model create(Iterable<? extends Pair<String, Object>> values) {
            return MutableModel.model(values);
        }

        public Model create(Map<String, Object> values) {
            return methods.fromMap(instance, values);
        }

        public Model create(String json) {
            return model(Json.parse(json));
        }

        public Iterable<?> toList(Iterable<?> map) {
            return sequence(map).toList();
        }
    }

    enum immutable implements ModelFactory {
        instance;

        public static Model model() {
            return instance.create();
        }

        public static Model model(Iterable<? extends Pair<String, Object>> values) {
            return instance.create(values);
        }

        public static Model model(Map<String, Object> values) {
            return instance.create(values);
        }

        public static Model parse(String json) {
            return instance.create(json);
        }

        public static Function1<ImmutableMap<String, Object>, Model> toModel() {
            return new Function1<ImmutableMap<String, Object>, Model>() {
                public Model call(ImmutableMap<String, Object> map) throws Exception {
                    return model(map);
                }
            };
        }

        public Model create() {
            return create(Sequences.<Pair<String, Object>>empty());
        }

        public Model create(Iterable<? extends Pair<String, Object>> values) {
            return ImmutableModel.model(values);
        }

        public Model create(Map<String, Object> values) {
            return methods.fromMap(instance, values);
        }

        public Model create(String json) {
            return model(Json.parse(json));
        }

        public Iterable<?> toList(Iterable<?> map) {
            return ImmutableList.constructors.reverse(map);
        }
    }

    class functions {
        public static Function2<Model, Pair<String, Object>, Model> updateValues() {
            return new Function2<Model, Pair<String, Object>, Model>() {
                public Model call(Model model, Pair<String, Object> value) throws Exception {
                    return model.set(value.first(), value.second());
                }
            };
        }

        public static final Function1<Model, Map<String, Object>> toMap = new Function1<Model, Map<String, Object>>() {
            public Map<String, Object> call(Model model) throws Exception {
                return model.toMap();
            }
        };

        public static Function1<? super Model, Map<String, Object>> asMap() {
            return toMap;
        }

        public static <T> Function1<? super Model, T> value(final String key, final Class<T> aClass) {
            return new Function1<Model, T>() {
                public T call(Model model) throws Exception {
                    return model.get(key, aClass);
                }
            };
        }

        public static Model toImmutableModel(Model model) {
            return immutable.model(model.toMap());
        }

        public static Model toMutableModel(Model model) {
            return mutable.model(model.toMap());
        }
    }

    class predicates {
        public static LogicalPredicate<Model> contains(final String key) {
            return new LogicalPredicate<Model>() {
                @Override
                public boolean matches(Model model) {
                    return model.contains(key);
                }
            };
        }

        public static LogicalPredicate<String> in(final Model model) {
            return new LogicalPredicate<String>() {
                @Override
                public boolean matches(String key) {
                    return model.contains(key);
                }
            };
        }
    }
}
