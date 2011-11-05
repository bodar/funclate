package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.BaseFunclate;
import com.googlecode.funclate.Model;
import com.googlecode.funclate.Renderer;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.net.URL;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.URLs.packageUrl;

public class StringTemplateFunclate extends BaseFunclate {
    private final EnhancedStringTemplateGroup group;

    public StringTemplateFunclate(Class base) {
        this(packageUrl(base));
    }

    public StringTemplateFunclate(URL base) {
        super();
        group = new EnhancedStringTemplateGroup(base);
    }

    @Override
    public Renderer<Object> get(final String name) {
        if (funclates.containsKey(name)) {
            return super.get(name);
        }
        return templateRenderer(group, name);
    }

    public static Renderer<Object> templateRenderer(final StringTemplateGroup group, final String name) {
        return new Renderer<Object>() {
            public String render(Object instance) throws Exception {
                return group.getInstanceOf(name, convertToMap(instance)).toString();
            }
        };
    }

    protected static Map convertToMap(Object value) {
        if (value instanceof Map) {
            return (Map) value;
        }
        if (value instanceof Model) {
            return ((Model) value).toMap();
        }
        return map(pair("value", value));
    }

}
