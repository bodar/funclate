package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Funclate;
import com.googlecode.funclate.Renderer;
import org.antlr.stringtemplate.AttributeRenderer;

public class RendererAdapter implements AttributeRenderer {
    private final Funclate funclate;

    public RendererAdapter(Funclate funclate) {
        this.funclate = funclate;
    }

    public String toString(Object value) {
        try {
            return funclate.render(value);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public String toString(Object value, String formats) {
        String[] parts = formats.split(",");
        Object currentResult = value;
        for (String format : parts) {
            currentResult = format(currentResult, format);
        }
        return currentResult.toString();
    }

    private String format(Object value, String name) {
        if(funclate.contains(name)) {
            try {
                return funclate.get(name).render(value);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
        throw new IllegalArgumentException(String.format("Invalid format argument: '%s'", name));
    }

    public static Renderer<Object> renderer(final AttributeRenderer renderer) {
        return new Renderer<Object>() {
            public String render(Object instance) throws Exception {
                return renderer.toString(instance);
            }
        };
    }
}
