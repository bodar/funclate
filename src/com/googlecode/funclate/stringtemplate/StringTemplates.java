package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Template;
import com.googlecode.funclate.Templates;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Strings;
import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;

public class StringTemplates implements Templates{
    private final EnhancedStringTemplateGroup group;

    public StringTemplates(URL baseUrl) {
        group = new EnhancedStringTemplateGroup(baseUrl);
    }

    public <T, R> Templates registerRenderer(Predicate<? super T> predicate, Callable1<T, R> callable) {
        group.registerRenderer(predicate, callable);
        return this;
    }

    public Template template(String name) {
        return new TemplateAdapter(group.getInstanceOf(name));
    }
}
