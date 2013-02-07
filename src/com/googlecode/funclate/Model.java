package com.googlecode.funclate;

import com.googlecode.funclate.json.Json;
import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.collections.PersistentList;
import com.googlecode.totallylazy.collections.PersistentMap;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.util.*;
import java.util.Properties;

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

    Iterable<Pair<String, Object>> pairs();

    <T> Pair<Model, Option<T>> remove(String key, Class<T> aClass);

    <T> Pair<Model, Option<T>> remove(String key);

    Model map(Callable1<? super Object, ?> callable);

    Model merge(Model other);


    enum mutable implements ModelFactory {
        instance;

        public static Model model() {
            return instance.create();
        }

        public static Model model(Iterable<? extends Pair<String, ? extends Object>> values) {
            return instance.create(values);
        }

        public static Model model(Map<String, ? extends Object> values) {
            return instance.create(values);
        }

        public static Model parse(String json) {
            return instance.create(json);
        }

        public Model create() {
            return create(Sequences.<Pair<String, Object>>empty());
        }

        public Model create(Iterable<? extends Pair<String, ? extends Object>> values) {
            return MutableModel.model(values);
        }

        public Model create(Map<String, ? extends Object> values) {
            return methods.fromMap(instance, values);
        }

        @Override
        public Model create(Properties properties) {
            return methods.fromProperties(instance, properties);
        }

        public Model create(String json) {
            return model(Json.parse(json));
        }

        public Iterable<?> toList(Iterable<?> map) {
            return sequence(map).toList();
        }
    }

    enum persistent implements ModelFactory {
        instance;

        public static Model model() {
            return instance.create();
        }

        public static Model model(Iterable<? extends Pair<String, ? extends Object>> values) {
            return instance.create(values);
        }

        public static Model model(Map<String, ? extends Object> values) {
            return instance.create(values);
        }

        public static Model parse(String json) {
            return instance.create(json);
        }

        public static final Function1<PersistentMap<String,? extends Object>,Model> toModel = new Function1<PersistentMap<String, ? extends Object>, Model>() {
            public Model call(PersistentMap<String, ? extends Object> map) throws Exception {
                return model(map);
            }
        };

        public static Function1<PersistentMap<String, ? extends Object>, Model> toModel() {
            return toModel;
        }

        public Model create() {
            return create(Sequences.<Pair<String, Object>>empty());
        }

        public Model create(Iterable<? extends Pair<String, ? extends Object>> values) {
            return PersistentModel.model(values);
        }

        public Model create(Map<String, ? extends Object> values) {
            return methods.fromMap(instance, values);
        }

        public Model create(String json) {
            return model(Json.parse(json));
        }

        @Override
        public Model create(Properties properties) {
            return methods.fromProperties(instance, properties);
        }

        public Iterable<?> toList(Iterable<?> map) {
            return PersistentList.constructors.reverse(map);
        }
    }

    class methods {
        public static Model toPersistentModel(Model model) {
            return persistent.model(model.toMap());
        }

        public static Model toMutableModel(Model model) {
            return mutable.model(model.toMap());
        }

        public static Map<String, Object> toMap(Model model) {
            Map<String, Object> result = new LinkedHashMap<String, Object>();
            for (Pair<String, Object> pair : model.pairs()) {
                result.put(pair.first(), toValue(pair.second()));
            }
            return result;
        }

        private static Object toValue(Object value) {
            if (value instanceof Model) return ((Model) value).toMap();
            if (value instanceof List) return Sequences.sequence((List<?>) value).map(toValue).toList();
            if (value instanceof PersistentList) return sequence(Unchecked.<PersistentList<Object>>cast(value)).map(toValue).reverse().toList();
            return value;
        }

        private static final Callable1<Object, Object> toValue = new Callable1<Object, Object>() {
            public Object call(Object o) throws Exception {
                return toValue(o);
            }
        };

        public static Model merge(Model into, Model other) {
            return sequence(other.pairs()).fold(into, functions.mergeEntry);
        }

        public static PersistentModel mergeFlattenChildren(Model into, Model other) {
            return (PersistentModel) sequence(toPersistentModel(other), toPersistentModel(into)).reduce(functions.mergeFlattenModel);
        }
    }

    class functions {

        private static final Function2<Model, Model, Model> mergeFlattenModel = new Function2<Model, Model, Model>() {
            @Override
            public Model call(Model model, Model model2) throws Exception {
                return sequence(model2.pairs()).fold(model, mergePairs);
            }
        };

        private static final Callable2<? super Model, ? super Pair<String, Object>, ? extends Model> mergePairs = new Callable2<Model, Pair<String, Object>, Model>() {
            @Override
            public Model call(Model into, Pair<String, Object> pair) throws Exception {
                final String        otherKey    = pair.first();
                Object              otherValue  = pair.second();
                List<Object>        intoValues  = into.getValues(otherKey);

                if (otherValue instanceof Model && intoValues.size() == 1 && intoValues.get(0) instanceof Model) {
                    Object folded = sequence(intoValues).safeCast(Model.class).fold((Model) otherValue, mergeFlattenModel);
                    return into.set(otherKey, folded);
                } else if (otherValue instanceof Iterable) {
                    Sequence<Object> others = Sequences.sequence(otherValue);
                    if (otherValue instanceof PersistentList) {
                        others = others.reverse();
                    }
                    return others.fold(into, add(otherKey));
                }
                return into.add(otherKey, otherValue);
            }
        };

        public static final Function3<String,Model,Object,Model> add = new Function3<String, Model, Object, Model>() {
            @Override
            public Model call(String key, Model model, Object o) throws Exception {
                return model.add(key, o);
            }
        };

        public static Function2<Model, Object, Model> add(final String key) {
            return Functions.uncurry2(add.apply(key));
        }


        public static final Function2<Model, Pair<String, Object>, Model> updateValues = new Function2<Model, Pair<String, Object>, Model>() {
            public Model call(Model model, Pair<String, Object> value) throws Exception {
                return model.set(value.first(), value.second());
            }
        };

        public static Function2<Model, Pair<String, Object>, Model> updateValues() {
            return updateValues;
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

        public static final Function2<Model,Model,Model> merge = new Function2<Model, Model, Model>() {
            public Model call(final Model result, Model part) throws Exception {
                return methods.merge(result, part);
            }
        };

        public static final Function2<Model,Model,Model> mergeFlattenChildren = new Function2<Model, Model, Model>() {
            public Model call(final Model result, Model part) throws Exception {
                return methods.mergeFlattenChildren(result, part);
            }
        };

        public static Function2<Model, Model, Model> mergeFlattenChildren() {
            return mergeFlattenChildren;
        }

        public static Function2<Model, Model, Model> merge() {
            return merge;
        }

        public static final Function2<Model,Pair<String,Object>,Model> mergeEntry = new Function2<Model, Pair<String, Object>, Model>() {
            public Model call(Model model, Pair<String, Object> pair) throws Exception {
                return model.add(pair.first(), pair.second());
            }
        };

        public static Function2< Model, Pair<String, Object>, Model> mergeEntry() {
            return mergeEntry;
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
