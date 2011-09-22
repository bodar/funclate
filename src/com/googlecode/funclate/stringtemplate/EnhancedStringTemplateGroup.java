package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Renderer;
import com.googlecode.funclate.Renderers;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.URLs.packageUrl;

public class EnhancedStringTemplateGroup extends StringTemplateGroup {
    private final Renderers renderers;

    public EnhancedStringTemplateGroup(URL baseUrl) {
        this(baseUrl, new Renderers());
    }

    public EnhancedStringTemplateGroup(Class classInPackage) {
        this(packageUrl(classInPackage), new Renderers());
    }

    public EnhancedStringTemplateGroup(URL baseUrl, Renderers renderers) {
        super(baseUrl.toString(), baseUrl.toString());
        this.renderers = renderers;
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

    static String format(String fileName) {
        if(fileName.startsWith("jar:")){
            return fileName.replaceFirst("(!.*)//(.*)$", "$1/$2");
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

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, Renderer<T> callable) {
        renderers.add(predicate, callable);
        return this;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        renderers.add(predicate, callable);
        return this;
    }
}