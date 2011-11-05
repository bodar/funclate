package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import org.junit.Test;

import java.net.URLEncoder;

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Strings.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BaseFunclateTest {
    @Test
    public void callsCorrectParentRender() throws Exception {
        Funclate parent = new BaseFunclate();
        parent.add(instanceOf(String.class), returns("BUG"));
        parent.add("encode", instanceOf(String.class), returns("Correct"));

        Funclate child = new BaseFunclate(parent);
        String result = child.get("encode").render("Hello Dan");
        assertThat(result, is("Correct"));
    }

    private Renderer<Object> returns(final String value) {
        return new Renderer<Object>() {
            public String render(Object instance) throws Exception {
                return value;
            }
        };
    }

    @Test
    public void supportsNamedFunclates() throws Exception {
        Funclate funclate = new BaseFunclate();
        funclate.add("encode", instanceOf(String.class), encode());
        String result = funclate.get("encode").render("Hello Dan");
        assertThat(result, is("Hello+Dan"));
    }

    @Test
    public void supportsImplicitFunclates() throws Exception {
        Funclate funclate = new BaseFunclate();
        funclate.add(instanceOf(String.class), encode());
        String result = funclate.render("Hello Dan");
        assertThat(result, is("Hello+Dan"));
    }

    @Test
    public void canIntegrateWithStringFormat() throws Exception {
        Funclate funclate = new BaseFunclate();
        funclate.add("format", instanceOf(String.class), format("Hello %s"));
        String result = funclate.get("format").render("Dan");
        assertThat(result, is("Hello Dan"));
    }


    private static Callable1<String, String> encode() {
        return new Callable1<String, String>() {
            public String call(String value) throws Exception {
                return URLEncoder.encode(value, "UTF-8");
            }
        };
    }
}
