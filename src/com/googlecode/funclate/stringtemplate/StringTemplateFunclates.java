package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.BaseFunclates;

import java.net.URL;

public class StringTemplateFunclates extends BaseFunclates {
    private final EnhancedStringTemplateGroup group;

    public StringTemplateFunclates(URL base) {
        group = new EnhancedStringTemplateGroup(base, renderers, true);
    }

    public String call(String name, Object value) throws Exception {
        if (funclates.containsKey(name)) {
            return super.call(name, value);
        }
        return String.valueOf(group.getInstanceOf(name, convertToMap(value)));
    }

}
