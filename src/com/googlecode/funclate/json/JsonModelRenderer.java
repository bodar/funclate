package com.googlecode.funclate.json;

import com.googlecode.funclate.Renderer;

public class JsonModelRenderer implements Renderer<Object> {
    public String render(Object value) throws Exception {
        return Json.toJson(value);
    }
}
