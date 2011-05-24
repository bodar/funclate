package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import org.junit.Test;

import javax.xml.ws.WebEndpoint;
import java.lang.annotation.Annotation;

import static com.googlecode.funclate.Model.model;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public abstract class TemplatesTests {
    protected abstract Templates templates();

    @Test
    public void supportsRenderers() throws Exception {
        String expected = "bar";
        Template template = templates().registerRenderer(instanceOf(String.class), returnsValue(expected)).template("test");
        assertThat(template.call(model().add("foo", "ignored")), is(expected));
    }

    @Test
    public void supportsRenderingAnnotations() throws Exception {
        Template template = templates().registerRenderer(instanceOf(WebEndpoint.class), name()).template("test");
        assertThat(template.call(model().add("foo", annotationInstance())), is("Hello"));
    }

    private static Callable1<WebEndpoint,String> name() {
        return new Callable1<WebEndpoint, String>() {
            public String call(WebEndpoint webEndpoint) throws Exception {
                return webEndpoint.name();
            }
        };
    }

    private static Callable1<String, String> returnsValue(final String value) {
        return new Callable1<String, String>() {
            public String call(String s) throws Exception {
                return value;
            }
        };
    }

    @WebEndpoint(name = "Hello")
    public static WebEndpoint annotationInstance() throws Exception {
        final WebEndpoint[] instance = new WebEndpoint[1];
        new Object() {{
            instance[0] = this.getClass().getEnclosingMethod().getAnnotation(WebEndpoint.class);
        }};

        return instance[0];
    }
}