package com.googlecode.funclate.parser;

import com.googlecode.funclate.CompositeFunclate;
import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemplateCallTest {
    @Test
    public void supportsToString() throws Exception {
        Grammar grammar = new Grammar(new CompositeFunclate());
        String template = "template(foo=bar, baz=\"dan\")";
        String toString = grammar.TEMPLATE_CALL.parse(template).toString();
        assertThat(toString, is(template));

    }
}
