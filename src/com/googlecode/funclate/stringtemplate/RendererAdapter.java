package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Renderer;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Xml;
import org.antlr.stringtemplate.AttributeRenderer;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.totallylazy.Callables.asString;

class RendererAdapter implements AttributeRenderer {
    private final Renderer<Object> renderer;
    public static final Map<String, Callable1<? super String, String>> ENCODERS = new ConcurrentHashMap<String, Callable1<? super String,String>> () {{
        put("xml", Xml.escape());
        put("html", Xml.escape());
        put("url", urlEncode());
        put("", asString());
    }};

    public RendererAdapter(Renderer<Object> renderer) {
        this.renderer = renderer;
    }

    private static Callable1<String, String> urlEncode() {
        return new Callable1<String, String>() {
            public String call(String s) throws Exception {
                return URLEncoder.encode(s, "UTF-8");
            }
        };
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
        String currentResult;
        if(parts[0].equals("raw")){
            currentResult = value.toString();
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        } else {
            currentResult = toString(value);
        }

        for (String part : parts) {
            currentResult = format(currentResult, part);
        }
        return currentResult;
    }

    private String format(String value, String format) {
        if(!ENCODERS.containsKey(format)){
            throw new IllegalArgumentException(String.format("Invalid format argument: '%s'", format));
        }
        try {
            return ENCODERS.get(format).call(value);
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }
}
