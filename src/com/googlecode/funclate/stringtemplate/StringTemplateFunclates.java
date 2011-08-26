package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.BaseFunclates;
import com.googlecode.funclate.Model;

import java.net.URL;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Pair.pair;

public class StringTemplateFunclates extends BaseFunclates {
    private final EnhancedStringTemplateGroup group;

    public StringTemplateFunclates(URL base) {
        group = new EnhancedStringTemplateGroup(base, renderers);
    }

    public String call(String name, Object value) throws Exception {
        if (funclates.containsKey(name)) {
            return super.call(name, value);
        }
        return String.valueOf(group.getInstanceOf(name, convertToMap(value)));
    }

}
