package com.googlecode.funclate.json;

import com.googlecode.funclate.Model;
import com.googlecode.funclate.json.grammar.Grammar;
import com.googlecode.totallylazy.Mapper;

import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Unchecked.cast;

public class Json {
    public static String toJson(Object value) {
        return StreamingJson.toJson(value, new StringBuilder()).toString();
    }

    public static String toJson(Model value) {
        return StreamingJson.toJson(value.toMap(), new StringBuilder()).toString();
    }

    public static String toJson(CharSequence value) {
        return Strings.toString(value);
    }

    public static String toArray(Iterable<?> values) {
        return StreamingJson.toJson(values, new StringBuilder()).toString();
    }

    public static String toObjectLiteral(Map<?, ?> map) {
        return StreamingJson.toJson(map, new StringBuilder()).toString();
    }

    public static Map<String, Object> parse(String json) {
        return map(json);
    }

    public static <V> Map<String, V> map(String json) {
        return cast(Grammar.OBJECT.parse(json));
    }

    public static <V> List<V> list(String json) {
        return cast(Grammar.ARRAY.parse(json));
    }

    public static Object object(String json) {
        return Grammar.VALUE.parse(json);
    }

    public static class functions {
        public static Mapper<String, Map<String, Object>> parse = new Mapper<String, Map<String, Object>>() {
            public Map<String, Object> call(String json) throws Exception {
                return Json.parse(json);
            }
        };

        public static Mapper<Object, String> toJson = new Mapper<Object, String>() {
            public String call(Object value) throws Exception {
                return Json.toJson(value);
            }
        };
    }
}
