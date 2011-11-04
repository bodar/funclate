package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Funclates;
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

import static com.googlecode.funclate.Funclates.methods.defaultFunclates;
import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.URLs.packageUrl;

public class EnhancedStringTemplateGroup extends StringTemplateGroup {
    private final boolean enableFormatsAsFunctions;
    private final Funclates funclates;

    public EnhancedStringTemplateGroup(URL baseUrl) {
        this(baseUrl, false);
    }

    public EnhancedStringTemplateGroup(URL baseUrl, boolean enableFormatsAsFunctions) {
        this(baseUrl, new Renderers(), enableFormatsAsFunctions);
    }

    public EnhancedStringTemplateGroup(Class classInPackage) {
        this(classInPackage, false);
    }

    public EnhancedStringTemplateGroup(Class classInPackage, boolean enableFormatsAsFunctions) {
        this(packageUrl(classInPackage), new Renderers(), enableFormatsAsFunctions);
    }

    public EnhancedStringTemplateGroup(URL baseUrl, Renderers renderers, boolean enableFormatsAsFunctions) {
        super(baseUrl.toString(), baseUrl.toString());
        this.enableFormatsAsFunctions = enableFormatsAsFunctions;
        funclates = defaultFunclates();
    }

    @Override
    public void setSuperGroup(StringTemplateGroup superGroup) {
        super.setSuperGroup(superGroup);
        if(superGroup instanceof EnhancedStringTemplateGroup){
            funclates.parent(((EnhancedStringTemplateGroup) superGroup).funclates);
        }
    }

    @Override
    public void registerRenderer(Class attributeClassType, Object instance) {
        throw new IllegalArgumentException(String.format("Please call 'registerRenderer(instanceOf(%s.class), renderer)' or 'registerRenderer(instanceOf(%1$s.class), 'format', renderer)'", attributeClassType.getSimpleName()));
    }

    @Override
    protected StringTemplate loadTemplate(final String name, String fileName) {
        if(enableFormatsAsFunctions && funclates.contains(name)) {
            return new ConvertTemplateToFunctionCall(funclates, name);
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
        return new RendererAdapter(funclates);
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, Renderer<? super T> callable) {
        funclates.add(predicate, callable);
        return this;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        funclates.add(predicate, callable);
        return this;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(String format, Predicate<? super T> predicate, Renderer<? super T> callable) {
        funclates.add(format, predicate, callable);
        return this;
    }

    public <T, R> EnhancedStringTemplateGroup registerRenderer(String format, Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        funclates.add(format, predicate, callable);
        return this;
    }


}