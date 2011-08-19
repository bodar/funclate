package com.googlecode.funclate.stringtemplate;

import com.googlecode.totallylazy.Callable1;

public class JsonModelRenderer implements Callable1<Object, String> {
    public String call(Object value) throws Exception {
        return Json.toJson(value);
    }

}
