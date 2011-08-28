package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.BaseFunclates;
import com.googlecode.funclate.Funclates;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;

import java.net.URL;
import java.util.Map;

public class StringFunclates extends BaseFunclates {
    private final EnhancedStringTemplateGroup group;
    public StringFunclates(URL base) {
        group = new EnhancedStringTemplateGroup(base, renderers);
    }

    public String call(String name, Object value) throws Exception {
        if(funclates.containsKey(name)){
            return super.call(name, value);
        }
        return String.valueOf(group.getInstanceOf(name, (Map) value));
    }
}
