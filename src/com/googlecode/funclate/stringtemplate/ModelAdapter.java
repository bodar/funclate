package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ModelAdapter extends AbstractMap {
    private final Model model;

    public ModelAdapter(Model model) {
        this.model = model;
    }

    private Object adapt(Object object) {
        if (object instanceof Model) {
            return new ModelAdapter((Model) object);
        }
        if (object instanceof Entry) {
            return adapt((Entry) object);
        }
        if (object instanceof Set) {
            return adapt((Set) object);
        }
        if (object instanceof List) {
            return adapt((List) object);
        }
        return object;
    }

    private List adapt(List object) {
        List adapted = new ArrayList();
        for (Object value : object) {
            adapted.add(adapt(value));
        }
        return adapted;
    }

    private Entry adapt(Entry object) {
        return new SimpleImmutableEntry(object.getKey(), adapt(object.getValue()));
    }

    private Set adapt(Set object) {
        Set adapted = new HashSet();
        for (Object o : object) {
            adapted.add(adapt(o));
        }
        return adapted;
    }

    @Override
    public Set entrySet() {
        return adapt(model.entries());
    }
}
