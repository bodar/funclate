package com.googlecode.funclate;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.Uri;

import java.io.IOException;
import java.net.URL;

public class StringFunclates extends CompositeFunclate{
    private final Uri baseUrl;

    public StringFunclates(URL baseUrl) {
        this.baseUrl = Uri.uri(baseUrl);
        create(NO_NAME);
    }

    @Override
    public Renderer<Object> get(String name) {
        if(contains(name)){
            return super.get(name);
        }
        try {
            Uri templateUri = baseUrl.mergePath(String.format("%s.fun", name));
            String template = Strings.toString(templateUri.toURL().openStream());
            return new StringFunclate(template, this);
        } catch (IOException e) {
            throw LazyException.lazyException(e);
        }
    }
}
