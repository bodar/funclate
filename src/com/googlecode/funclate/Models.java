package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable2;

import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Models {

    public static Callable2<Model, Model, Model> merge() {
        return new Callable2<Model, Model, Model>() {
            public Model call(final Model result, Model part) throws Exception {
                return sequence(part.entries()).fold(result, mergeEntry());
            }
        };
    }

    private static Callable2<? super Model, ? super Map.Entry<String, Object>, Model> mergeEntry() {
        return new Callable2<Model, Map.Entry<String, Object>, Model>() {
            public Model call(Model model, Map.Entry<String, Object> entry) throws Exception {
                return model.add(entry.getKey(), entry.getValue());
            }
        };
    }
}
