package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public abstract class TemplatesTests {
    protected abstract Templates templates();

    @Test
    public void supportsRenderers() throws Exception {
        String expected = "bar";
        Template template = templates().registerRenderer(instanceOf(String.class), returnsValue(expected)).template("test");
        assertThat(template.call(record().set(keyword("foo"), "ignored")).toString(), is(expected));
    }

    private static Callable1<String, String> returnsValue(final String value) {
        return new Callable1<String, String>() {
            public String call(String s) throws Exception {
                return value;
            }
        };
    }
}
