package com.googlecode.funclate.stringtemplate;

import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class RecordAdapter implements Map {
    private final Record record;

    public RecordAdapter(Record record) {
        this.record = record;
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        return record.keywords().contains(toKey(key));
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        return record.get(toKey(key));
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

    private Keyword<Object> toKey(Object key) {
        return Keyword.keyword(key.toString());
    }
}
