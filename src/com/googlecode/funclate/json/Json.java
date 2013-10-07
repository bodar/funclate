package com.googlecode.funclate.json;

import com.googlecode.funclate.Model;
import com.googlecode.funclate.json.grammar.Grammar;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Mapper;

import java.util.Map;

import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;

public class Json {
    public static final String SEPARATOR = ",";

    public static String toJson(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return quote(escape((String) value));
        if (value instanceof Map) return toObjectLiteral((Map) value);
        if (value instanceof Model) return toObjectLiteral(((Model) value).toMap());
        if (value instanceof Iterable) return toArray((Iterable) value);
        if (value instanceof Boolean) return value.toString();
        if (value instanceof Number) return value.toString();
        return quote(escape(value.toString()));
    }

    public static String escape(String value) {
        return characters(value).map(functions.escape).toString("");
    }

    public static String escape(Character character) {
        switch (character) {
            case '"': return "\\\"";
            case '\\': return "\\\\";
            case '\b': return "\\b";
            case '\n': return "\\n";
            case '\r': return "\\r";
            case '\t': return "\\t";
            default: return character.toString();
        }
    }

    public static String quote(String value) {
        return format("\"%s\"", value);
    }

    public static String toPair(Object key, Object value) {
        return format("%s:%s", quote(String.valueOf(key)), toJson(value));
    }

    public static String toArray(Iterable<?> values) {
        return sequence(values).map(functions.toJson).toString("[", SEPARATOR, "]");
    }

    public static String toObjectLiteral(Map<?, ?> map) {
        return sequence(map.entrySet()).map(functions.toPair).toString("{", SEPARATOR, "}");
    }

    @Deprecated //use functions.toJson() removed after version 182
    public static Callable1<Object, String> toJson() {
        return new Callable1<Object, String>() {
            public String call(Object value) throws Exception {
                return toJson(value);
            }
        };
    }

    public static Map<String, Object> parse(String json) {
        return Grammar.OBJECT.parse(json);
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

        public static Mapper<Character, String> escape = new Mapper<Character, String>() {
            public String call(Character character) throws Exception {
                return escape(character);
            }
        };

        public static Mapper<Map.Entry, String> toPair = new Mapper<Map.Entry, String>() {
            public String call(Map.Entry entry) throws Exception {
                return toPair(entry.getKey(), entry.getValue());
            }
        };
    }
}
