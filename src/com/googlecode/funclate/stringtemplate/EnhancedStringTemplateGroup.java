package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Templates;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Strings;
import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URL;

public class EnhancedStringTemplateGroup extends StringTemplateGroup {
    private final Renderers renderers = new Renderers();

    public EnhancedStringTemplateGroup(URL baseUrl) {
        super(baseUrl.toString(), baseUrl.toString());
    }

    @Override
    protected StringTemplate loadTemplate(String name, String fileName) {
        try {
            String text = Strings.toString(new URL(format(fileName)).openStream());
            return loadTemplate(name, new BufferedReader(new StringReader(text)));
        } catch (Exception e) {
            return null;
        }
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
            return renderers;
        }
        return attributeRenderer;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, Callable1<T, R> callable) {
        renderers.add(predicate, callable);
        return this;
    }



}
