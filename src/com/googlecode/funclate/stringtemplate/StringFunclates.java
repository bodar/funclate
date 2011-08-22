package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Funclates;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;

import java.net.URL;
import java.util.Map;

public class StringFunclates implements Funclates{
    private final EnhancedStringTemplateGroup group;
    public StringFunclates(URL base) {
        group = new EnhancedStringTemplateGroup(base);
    }

    public Funclates add(String name, Callable1<?, String> callable) {
        throw new UnsupportedOperationException();
    }

    public <T> Funclates add(Predicate<? super T> predicate, Callable1<T, ?> encode) {
        throw new UnsupportedOperationException();
    }

    public String call(Object o) throws Exception {
        throw new UnsupportedOperationException();
    }

    public String call(String name, Object value) throws Exception {
        return group.getInstanceOf(name, (Map) value).toString();
    }
}
