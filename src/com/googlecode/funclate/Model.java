package com.googlecode.funclate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Model {
    private final Map<String, Object> values = new HashMap<String, Object>();

    public static Model model() {
        return new Model();
    }

    public <T> Model add(String key, T value) {
        if(!contains(key)){
            values.put(key, value);
            return this;
        }
        List list = getValues(key, value.getClass());
        list.add(value);
        values.put(key, list);
        return this;
    }

    public boolean contains(String key) {
        return values.containsKey(key);
    }

    public Object get(String key) {
        return values.get(key);
    }

    public <T> T get(String key, Class<T> aClass) {
        T t = (T) values.get(key);
        if(t instanceof List){
            return (T) ((List) t).get(0);
        }
        return t;
    }

    public <T> List<T> getValues(String key, Class<T> aClass) {
        final Object value = get(key);
        if(value instanceof List){
            return (List) value;
        }
        return new ArrayList(){{
            add(value);
        }};
    }

}
