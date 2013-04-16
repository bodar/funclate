package com.googlecode.funclate;

import com.googlecode.totallylazy.Strings;

public interface RendererContainer {
    Renderer<Object> get(String name);

    public static class methods {
        public static RendererContainer noParent() {
            return new RendererContainer() {
                public Renderer<Object> get(String name) {
                    return new Renderer<Object>() {
                        public String render(Object instance) throws Exception {
                            return Strings.asString(instance);
                        }
                    };
                }
            };
        }
    }
}
