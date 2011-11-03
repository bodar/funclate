package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Renderer;
import com.googlecode.funclate.Renderers;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Xml;
import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.funclate.stringtemplate.RendererAdapter.*;
import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.URLs.packageUrl;

public class EnhancedStringTemplateGroup extends StringTemplateGroup {
    private final Renderers renderers;
    private final Map<String, Renderers> namedRenderers;

    public EnhancedStringTemplateGroup(URL baseUrl) {
        this(baseUrl, new Renderers());
    }

    public EnhancedStringTemplateGroup(Class classInPackage) {
        this(packageUrl(classInPackage), new Renderers());
    }

    public EnhancedStringTemplateGroup(URL baseUrl, Renderers renderers) {
        super(baseUrl.toString(), baseUrl.toString());
        this.renderers = renderers;
        namedRenderers = defaultEncoders();
    }

    public static HashMap<String, Renderers> defaultEncoders() {
        return new HashMap<String, Renderers>(){{
            put("", new Renderers().add(always(), Callables.asString()));
            put("raw", new Renderers().add(always(), Callables.asString()));
            put("html", new Renderers().add(always(), Xml.escape()));
            put("xml", new Renderers().add(always(), Xml.escape()));
            put("url", new Renderers().add(always(), urlEncode()));
        }};
    }

    @Override
    public void setSuperGroup(StringTemplateGroup superGroup) {
        super.setSuperGroup(superGroup);
        if(superGroup instanceof EnhancedStringTemplateGroup){
            renderers.parent(((EnhancedStringTemplateGroup) superGroup).renderers);
        }
    }

    @Override
    public void registerRenderer(Class attributeClassType, Object instance) {
        throw new IllegalArgumentException(String.format("Please call 'registerRenderer(instanceOf(%s.class), renderer)' or 'registerRenderer(instanceOf(%1$s.class), 'format', renderer)'", attributeClassType.getSimpleName()));
    }

    public static Renderer<Object> renderer(final AttributeRenderer renderer) {
        return new Renderer<Object>() {
            public String render(Object instance) throws Exception {
                return renderer.toString(instance);
            }
        };
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
        if (fileName.startsWith("jar:")) {
            return fileName.replaceFirst("(!.*)//(.*)$", "$1/$2");
        }
        return fileName;
    }

    @Override
    public AttributeRenderer getAttributeRenderer(Class attributeClassType) {
        return new RendererAdapter(renderers, namedRenderers);
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, Renderer<? super T> callable) {
        renderers.add(predicate, callable);
        return this;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        renderers.add(predicate, callable);
        return this;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, String format, Renderer<? super T> callable) {
        get(format).add(predicate, callable);
        return this;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, String format, Callable1<? super T, String> callable) {
        get(format).add(predicate, callable);
        return this;
    }

    private Renderers get(String name) {
        if(!namedRenderers.containsKey(normalise(name))) {
            namedRenderers.put(normalise(name), new Renderers());
        }
        return namedRenderers.get(normalise(name));
    }

    private static Callable1<String, String> urlEncode() {
        return new Callable1<String, String>() {
            public String call(String s) throws Exception {
                return URLEncoder.encode(s, "UTF-8");
            }
        };
    }
}