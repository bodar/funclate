package com.googlecode.funclate.json;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.multi;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Appendables.append;
import static com.googlecode.totallylazy.LazyException.lazyException;

public class StreamingJson {
    public static final String SEPARATOR = ",";

    public static <A extends Appendable> A toJson(final Object o, final A appendable) {
        return new multi(){}.<A>methodOption(o, appendable).getOrElse(new Callable<A>() {
            @Override
            public A call() throws Exception {
                return toJson(o.toString(), appendable);
            }
        });
    }

    @multimethod
    public static <A extends Appendable> A toJson(final CharSequence charSequence, final A appendable) {
        return append(Strings.toString(charSequence), appendable);
    }

    @multimethod
    public static <A extends Appendable> A toJson(final Iterator<?> iterator, final A appendable) {
        return iterate(iterator, appendable, "[", SEPARATOR, "]");
    }

    @multimethod
    public static <A extends Appendable> A toJson(final Iterable<?> iterable, final A appendable) {
        return toJson(iterable.iterator(), appendable);
    }

    @multimethod
    public static <A extends Appendable> A toJson(final Model model, final A appendable) {
        return toJson(model.toMap(), appendable);
    }

    @multimethod
    public static <A extends Appendable> A toJson(final Map<?, ?> map, final A appendable) {
        return iterate(map.entrySet().iterator(), appendable, "{", SEPARATOR, "}");
    }

    @multimethod
    public static <A extends Appendable> A toJson(final Map.Entry<?, ?> entry, final A appendable) {
        return toJson(entry.getValue(), append(':', append(Strings.toString(String.valueOf(entry.getKey())), appendable)));
    }

    @multimethod
    public static <A extends Appendable> A toJson(final Void aVoid, final A appendable) {
        return append("null", appendable);
    }

    @multimethod
    public static <A extends Appendable> A toJson(final Number number, final A appendable) {
        return append(number.toString(), appendable);
    }

    @multimethod
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
}