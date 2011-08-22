package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Renderer;
import org.antlr.stringtemplate.AttributeRenderer;

class RendererAdapter implements AttributeRenderer {
    private final Renderer<Object> renderer;

    public RendererAdapter(Renderer<Object> renderer) {
        this.renderer = renderer;
    }

    public String toString(Object value) {
        try {
            return renderer.render(value);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public String toString(Object value, String format) {
        return toString(value);
    }
}
