package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ModelAdapter implements Map {
    private final Model model;

    public ModelAdapter(Model model) {
        this.model = model;
    }

    public int size() {
        throw new UnsupportedOperationException("size");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("isEmpty");
    }

    public boolean containsKey(Object key) {
        return model.contains(key.toString());
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("containsValue");
    }

    public Object get(Object key) {
        return model.getObject(key.toString());
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException("put");
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException("remove");
    }

    public void putAll(Map m) {
        throw new UnsupportedOperationException("putAll");
    }

    public void clear() {
        throw new UnsupportedOperationException("clear");
    }

    public Set keySet() {
        throw new UnsupportedOperationException("keySet");
    }

    public Collection values() {
        throw new UnsupportedOperationException("values");
    }

    public Set entrySet() {
        throw new UnsupportedOperationException("entrySet");
    }
}
