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
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        return model.contains(key.toString());
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        return model.get(key.toString());
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map m) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    private String toKey(Object key) {
        return key.toString();
    }
}
