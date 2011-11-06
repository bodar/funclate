package com.googlecode.funclate;

import com.googlecode.funclate.parser.Grammar;

import java.util.Map;

import static com.googlecode.funclate.RendererContainer.methods.noParent;
import static com.googlecode.totallylazy.Predicates.instanceOf;

public class StringFunclate extends CompositeFunclate {
    public StringFunclate(String template) {
        this(template, noParent());
    }

    public StringFunclate(String template, RendererContainer parent) {
        super(parent);
        add(instanceOf(Map.class), Grammar.TEMPLATE(this).parse(template));
        add(instanceOf(Model.class), new ModelRenderer(this));
    }

    public static class ModelRenderer implements Renderer<Model> {
        private final Funclate funclate;

        public ModelRenderer(Funclate funclate) {
            this.funclate = funclate;
        }

        public String render(Model model) throws Exception {
            return funclate.render(model.toMap());
        }
    }
}
