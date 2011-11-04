package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Funclates;
import com.googlecode.funclate.Renderer;
import org.antlr.stringtemplate.AttributeRenderer;

public class RendererAdapter implements AttributeRenderer {
    private final Funclates funclates;

    public RendererAdapter(Funclates funclates) {
        this.funclates = funclates;
    }

    public String toString(Object value) {
        try {
            return funclates.render(value);
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
        if(funclates.contains(name)) {
            try {
                return funclates.get(name).render(value);
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
