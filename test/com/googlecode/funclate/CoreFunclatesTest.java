package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import org.junit.Test;

import java.net.URLEncoder;

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CoreFunclatesTest {
    @Test
    public void supportsNamedFunclates() throws Exception {
        Funclates funclates = new CoreFunclates();
        funclates.add("encode", encode());
        String result = funclates.call("encode", "Hello Dan");
        assertThat(result, is("Hello+Dan"));
    }

    @Test
    public void supportsImplicitFunclates() throws Exception {
        Funclates funclates = new CoreFunclates();
        funclates.add(instanceOf(String.class), encode());
        String result = funclates.call("Hello Dan");
        assertThat(result, is("Hello+Dan"));
    }

    private static Callable1<String, String> encode() {
        return new Callable1<String, String>(){
            public String call(String value) throws Exception {
                return URLEncoder.encode(value, "UTF-8");
            }
        };
    }
}
