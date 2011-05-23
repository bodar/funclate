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

public class StringTemplates extends StringTemplateGroup implements Templates{
    private final Renderers renderers = new Renderers();

    public StringTemplates(URL baseUrl) {
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
            return fileName.replace("//", "/");
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

    public <T, R> Templates registerRenderer(Predicate<? super T> predicate, Callable1<T, R> callable) {
        renderers.add(predicate, callable);
        return this;
    }

    public Template template(String name) {
        return new TemplateAdapter(getInstanceOf(name));
    }
}
