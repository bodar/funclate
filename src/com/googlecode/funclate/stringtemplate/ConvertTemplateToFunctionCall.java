package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Funclates;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateWriter;
import org.antlr.stringtemplate.language.FormalArgument;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConvertTemplateToFunctionCall extends StringTemplate {
    private final Funclates funclates;
    private final String functionName;

    public ConvertTemplateToFunctionCall(Funclates funclates, String functionName) {
        super("$item$");
        this.funclates = funclates;
        this.functionName = functionName;
    }

    @Override
    public StringTemplate getInstanceOf() {
        return this;
    }

    @Override
    public int write(StringTemplateWriter out) throws IOException {
        try {
            Object value = argumentContext.get("item");
            String rendered = funclates.get(functionName).render(value);
            return out.write(rendered);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public Map getFormalArguments() {
        return new HashMap(){{
            put("item", new FormalArgument("item"));
        }};
    }
}
