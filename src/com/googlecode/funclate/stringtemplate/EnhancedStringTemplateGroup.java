package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.CompositeFunclate;
import com.googlecode.funclate.Funclate;
import com.googlecode.funclate.Renderer;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static com.googlecode.funclate.Funclate.methods.addDefaultEncoders;
import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.URLs.packageUrl;

public class EnhancedStringTemplateGroup extends StringTemplateGroup {
    private boolean enableFormatsAsFunctions = false;
    private final Funclate funclate;

    public EnhancedStringTemplateGroup(Class classInPackage) {
        this(packageUrl(classInPackage));
    }

    public EnhancedStringTemplateGroup(URL baseUrl) {
        this(baseUrl, (StringTemplateGroup) null);
    }

    public EnhancedStringTemplateGroup(Class classInPackage, StringTemplateGroup parent) {
        this(packageUrl(classInPackage), parent);
    }

    public EnhancedStringTemplateGroup(URL baseUrl, StringTemplateGroup parent) {
        this(baseUrl, addDefaultEncoders(createFunclates(parent)));
        super.setSuperGroup(parent);
    }

    public EnhancedStringTemplateGroup(URL baseUrl, Funclate parent) {
        super(baseUrl.toString(), baseUrl.toString());
        funclate = parent;
    }

    private static Funclate createFunclates(StringTemplateGroup parent) {
        if (parent instanceof EnhancedStringTemplateGroup) {
            return new CompositeFunclate(((EnhancedStringTemplateGroup) parent).funclate);
        }
        return new CompositeFunclate();
    }

    public EnhancedStringTemplateGroup enableFormatsAsFunctions() {
        this.enableFormatsAsFunctions = true;
        return this;
    }

    @Override
    @Deprecated
    public void setSuperGroup(StringTemplateGroup superGroup) {
        throw new UnsupportedOperationException("Please use the constructor to set the super goup");
    }

    @Override
    @Deprecated
    public void registerRenderer(Class attributeClassType, Object instance) {
        throw new IllegalArgumentException(String.format("Please call 'registerRenderer(instanceOf(%s.class), renderer)' or 'registerRenderer(instanceOf(%1$s.class), 'format', renderer)'", attributeClassType.getSimpleName()));
    }

    @Override
    protected StringTemplate loadTemplate(final String name, String fileName) {
        if (enableFormatsAsFunctions && funclate.contains(name)) {
            return new ConvertTemplateToFunctionCall(funclate, name);
        }
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
        return new RendererAdapter(funclate);
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, Renderer<? super T> callable) {
        funclate.add(predicate, callable);
        return this;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        funclate.add(predicate, callable);
        return this;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(String format, Predicate<? super T> predicate, Renderer<? super T> callable) {
        funclate.add(format, predicate, callable);
        return this;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(String format, Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        funclate.add(format, predicate, callable);
        return this;
    }


}