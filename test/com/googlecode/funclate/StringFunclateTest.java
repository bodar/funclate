package com.googlecode.funclate;

import org.hamcrest.Matchers;
import org.junit.Test;

import static com.googlecode.funclate.Model.mutable.model;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringFunclateTest {
    @Test
    public void canParseTemplateViaConstructor() throws Exception {
        StringFunclate funclate = new StringFunclate("Hello $name$");
        assertThat(funclate.render(model().add("name", "Dan")), Matchers.is("Hello Dan"));
    }
}
