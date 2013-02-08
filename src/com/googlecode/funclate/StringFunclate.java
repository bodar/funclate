package com.googlecode.funclate;

import com.googlecode.funclate.parser.Grammar;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicates;

import java.util.Map;

import static com.googlecode.funclate.RendererContainer.methods.noParent;
import static com.googlecode.totallylazy.Predicates.instanceOf;

public class StringFunclate extends CompositeFunclate {
    public StringFunclate(String template) {
        this(template, noParent());
    }

    public StringFunclate(String template, RendererContainer parent) {
        super(parent);
        add(instanceOf(Map.class), new Grammar(this).parse(template));
        add(instanceOf(Model.class), new ModelRenderer(this));
//        add("if", instanceOf(Map.class), ifElse());
    }

    private Callable1<Map<String, String>, String> ifElse() {
        return new Callable1<Map<String, String>, String>() {
            @Override
            public String call(Map<String, String> arguments) throws Exception {
                return "TODO";
            }
        };
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

    public static class functions {
        public static Renderer<Map<String, String>> first(final Callable1<String, String> callable) {
            return new Renderer<Map<String, String>>() {
                @Override
                public String render(Map<String, String> instance) throws Exception {
                    return callable.call(instance.get("0"));
                }
            };
        }
    }
}
