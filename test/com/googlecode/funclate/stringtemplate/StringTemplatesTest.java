package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Templates;
import com.googlecode.funclate.TemplatesTests;

import java.net.MalformedURLException;
import java.net.URL;

import static com.googlecode.totallylazy.Strings.EMPTY;

public class StringTemplatesTest extends TemplatesTests {
    @Override
    protected Templates templates() {
        return new StringTemplates(packageUrl(StringTemplatesTest.class));
    }

    private URL packageUrl(final Class<?> aClass) {
        String name = aClass.getSimpleName() + ".class";
        try {
            return new URL(aClass.getResource(name).toString().replace(name, EMPTY));
        } catch (MalformedURLException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
