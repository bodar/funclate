package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Renderers;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Strings;
import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.io.*;
import java.net.URL;

import static com.googlecode.totallylazy.Closeables.using;

public class EnhancedStringTemplateGroup extends StringTemplateGroup {
    private final Renderers renderers = new Renderers();

    public EnhancedStringTemplateGroup(URL baseUrl) {
        super(baseUrl.toString(), baseUrl.toString());
    }

    @Override
    protected StringTemplate loadTemplate(final String name, String fileName) {
        try {
            return using(new URL(format(fileName)).openStream(), loadTemplate(name));
        } catch (Exception e) {
            return null;
        }
    }

    private Callable1<InputStream, StringTemplate> loadTemplate(final String name) {
        return new Callable1<InputStream, StringTemplate>() {
            public StringTemplate call(InputStream stream) throws Exception {
                return loadTemplate(name, new BufferedReader(new InputStreamReader(stream)));
            }
        };
    }

    private String format(String fileName) {
        if(fileName.startsWith("jar:")){
            return fileName.replaceFirst("//([^/]+)$", "/$1");
        }
        return fileName;
    }

    @Override
    public AttributeRenderer getAttributeRenderer(Class attributeClassType) {
        AttributeRenderer attributeRenderer = super.getAttributeRenderer(attributeClassType);
        if(attributeRenderer == null){
            return new RendererAdapter(renderers);
        }
        return attributeRenderer;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, Callable1<T, R> callable) {
        renderers.add(predicate, callable);
        return this;
    }


}
