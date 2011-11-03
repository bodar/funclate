package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Renderer;
import com.googlecode.funclate.Renderers;
import org.antlr.stringtemplate.AttributeRenderer;

import java.util.Map;

class RendererAdapter implements AttributeRenderer {
    private final Renderer<Object> renderer;
    private final Map<String, Renderers> namedRenderers;

    public RendererAdapter(Renderer<Object> renderer, Map<String, Renderers> namedRenderers) {
        this.renderer = renderer;
        this.namedRenderers = namedRenderers;
    }

    public String toString(Object value) {
        try {
            return renderer.render(value);
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

    private String format(Object value, String format) {
        if(namedRenderers.containsKey(format)) {
            try {
                return namedRenderers.get(format).render(value);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
        throw new IllegalArgumentException(String.format("Invalid format argument: '%s'", format));
    }
}
