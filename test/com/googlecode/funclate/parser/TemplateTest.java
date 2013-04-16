package com.googlecode.funclate.parser;

import com.googlecode.funclate.CompositeFunclate;
import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemplateTest {
    @Test
    public void supportsToString() throws Exception {
        Grammar grammar = new Grammar(new CompositeFunclate());
        String template = "Hello $name$ $template()$ $yourLastName(template())$";
        String toString = grammar.parse(template).toString();
        assertThat(toString, is(template));

    }
}
