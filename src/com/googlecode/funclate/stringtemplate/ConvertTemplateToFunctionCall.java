package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Renderers;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateWriter;
import org.antlr.stringtemplate.language.FormalArgument;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConvertTemplateToFunctionCall extends StringTemplate {
    private final Map<String, Renderers> namedRenderers;
    private final String functionName;

    public ConvertTemplateToFunctionCall(Map<String, Renderers> namedRenderers, String functionName) {
        super("$item$");
        this.namedRenderers = namedRenderers;
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
            String rendered = namedRenderers.get(functionName).render(value);
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
