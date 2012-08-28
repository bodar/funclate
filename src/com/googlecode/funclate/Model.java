package com.googlecode.funclate;

import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Model {
    <T> Model add(String key, T value);

    <T> Model set(String name, T value);

    <T> T get(String key, Class<T> aClass);

    <T> T get(String key);

    <T> List<T> getValues(String key, Class<T> aClass);

    <T> List<T> getValues(String key);

    <T> T getObject(String key);

    boolean contains(String key);

    Model copy();

    Map<String, Object> toMap();

    Set<Map.Entry<String, Object>> entries();

    <T> Pair<Model, T> remove(String key, Class<T> aClass);

    <T> Pair<Model, T> remove(String key);

    public static final class mutable {
        private mutable() {
        }

        public static Model model() {
            return MutableModel.model(Sequences.<Pair<String, Object>>empty());
        }

        public static Model model(Iterable<? extends Pair<String, Object>> values) {
            return MutableModel.model(values);
        }
    }

    public static final class immutable {
        private immutable() {
        }

        public static Model model() {
            return model(Sequences.<Pair<String, Object>>empty());
        }

        public static Model model(Iterable<? extends Pair<String, Object>> values) {
            return ImmutableModel.model(values);
        }
    }

    public static final class functions {
        private functions() {}

        public static Function2<Model, Pair<String, Object>, Model> updateValues() {
            return new Function2<Model, Pair<String, Object>, Model>() {
                public Model call(Model model, Pair<String, Object> value) throws Exception {
                    return model.set(value.first(), value.second());
                }
            };
        }
    }
}
