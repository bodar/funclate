package com.googlecode.funclate;

import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.MapRecord;
import com.googlecode.totallylazy.records.Record;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Arrays.list;
import static com.googlecode.totallylazy.records.Keyword.keyword;

public class Model {
    private final Record record = new MapRecord();

    public static Model model() {
        return new Model();
    }

    public Model add(String key, Object value) {
        Keyword<Object> keyword = keyword(key);
        if(!record.keywords().contains(keyword)){
            record.set(keyword, value);
            return this;
        }
        List list = listFor(keyword);
        list.add(value);
        record.set(keyword, list);
        return this;
    }

    private List listFor(Keyword<Object> keyword) {
        final Object value = record.get(keyword);
        if(value instanceof List){
            return (List) value;
        }
        return new ArrayList(){{
            add(value);
        }};
    }

    public <T> T get(String key, Class<T> aClass) {
        T t = record.get(keyword(key, aClass));
        if(t instanceof List){
            return (T) ((List) t).get(0);
        }
        return t;
    }

    public <T> List<T> getValues(String key, Class<T> aClass) {
        return listFor(keyword(key));
    }
}
