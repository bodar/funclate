package com.googlecode.funclate.json;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.annotations.multimethod;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import static com.googlecode.totallylazy.LazyException.lazyException;

public class StreamingJson {
    public static void toJson(final Object o, final Writer writer) {
        if(o instanceof Iterator) {
            toJson((Iterator) o, writer);
            return;
        }
        if(o instanceof Iterable) {
            toJson(((Iterable) o), writer);
            return;
        }
        if(o instanceof Map) {
            toJson((Map) o, writer);
            return;
        }
        if(o instanceof Model) {
            toJson(((Model) o).toMap(), writer);
            return;
        }
        try {
            writer.write(Json.toJson(o));
        } catch (IOException e) {
            throw lazyException(e);
        }
    }

    @multimethod
    public static void toJson(Iterator<?> iterator, Writer writer) {
        iterate(iterator, writer, '[', ',', ']');
    }

    @multimethod
    public static void toJson(Iterable<?> iterable, Writer writer) {
        toJson(iterable.iterator(), writer);
    }

    @multimethod
    public static void toJson(Map<?,?> map, Writer writer) {
        iterate(map.entrySet().iterator(), writer, '{', ',', '}');
    }

    private static void iterate(Iterator<?> iterator, Writer writer, char start, char separator, char end) {
        try {
            writer.write(start);
            if (iterator.hasNext()) toJson(iterator.next(), writer);
            while (iterator.hasNext()) {
                writer.write(separator);
                toJson(iterator.next(), writer);
            }
            writer.write(end);
        } catch (IOException e) {
            throw lazyException(e);
        }
    }
}

