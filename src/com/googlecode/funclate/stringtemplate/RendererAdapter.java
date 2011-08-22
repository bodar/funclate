package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Renderers;
import org.antlr.stringtemplate.AttributeRenderer;

class RendererAdapter implements AttributeRenderer {
    private final Renderers renderers;

    public RendererAdapter(Renderers renderers) {
        this.renderers = renderers;
    }

    public String toString(Object value) {
        return renderers.call(value);
    }

    public String toString(Object value, String format) {
        return toString(value);
    }
}
