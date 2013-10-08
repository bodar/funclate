package com.googlecode.funclate.json;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Mapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static com.googlecode.totallylazy.LazyException.lazyException;

public class StreamingJson {
    public static final String SEPARATOR = ",";

    public static <A extends Appendable> A toJson(final Object o, final A appendable) {
        if (o == null) return toJson((Void) null, appendable);
        if (o instanceof CharSequence) return toJson((CharSequence) o, appendable);
        if (o instanceof Number) return toJson((Number) o, appendable);
        if (o instanceof Boolean) return toJson((Boolean) o, appendable);
        if (o instanceof Map) return toJson((Map) o, appendable);
        if (o instanceof Map.Entry) return toJson((Map.Entry) o, appendable);
        if (o instanceof Iterator) return toJson((Iterator) o, appendable);
        if (o instanceof Iterable) return toJson(((Iterable) o), appendable);
        if (o instanceof Model) return toJson(((Model) o), appendable);
        return toJson(o.toString(), appendable);
    }

    public static <A extends Appendable> A toJson(final CharSequence charSequence, final A appendable) {
        return append(Strings.toString(charSequence), appendable);
    }

    public static <A extends Appendable> A toJson(final Iterator<?> iterator, final A appendable) {
        return iterate(iterator, appendable, "[", SEPARATOR, "]");
    }

    public static <A extends Appendable> A toJson(final Iterable<?> iterable, final A appendable) {
        return toJson(iterable.iterator(), appendable);
    }

    public static <A extends Appendable> A toJson(final Model model, final A appendable) {
        return toJson(model.toMap(), appendable);
    }

    public static <A extends Appendable> A toJson(final Map<?, ?> map, final A appendable) {
        return iterate(map.entrySet().iterator(), appendable, "{", SEPARATOR, "}");
    }

    public static <A extends Appendable> A toJson(final Map.Entry<?, ?> entry, final A appendable) {
        return toJson(entry.getValue(), append(':', append(Strings.toString(String.valueOf(entry.getKey())), appendable)));
    }

    public static <A extends Appendable> A toJson(final Void aVoid, final A appendable) {
        return append("null", appendable);
    }

    public static <A extends Appendable> A toJson(final Number number, final A appendable) {
        return append(number.toString(), appendable);
    }

    public static <A extends Appendable> A toJson(final Boolean bool, final A appendable) {
        return append(bool.toString(), appendable);
    }

    private static <A extends Appendable> A iterate(final Iterator<?> iterator, final A appendable, String start, String separator, String end) {
        return Iterators.appendTo(Iterators.map(iterator, new Mapper<Object, String>() {
            @Override
            public String call(Object o) throws Exception {
                toJson(o, appendable);
                return "";
            }
        }), appendable, start, separator, end);
    }

    private static <A extends Appendable> A append(CharSequence charSequence, A appendable) {
        try {
            appendable.append(charSequence);
            return appendable;
        } catch (IOException e) {
            throw lazyException(e);
        }
    }

    private static <A extends Appendable> A append(char character, A appendable) {
        try {
            appendable.append(character);
            return appendable;
        } catch (IOException e) {
            throw lazyException(e);
        }
    }

}