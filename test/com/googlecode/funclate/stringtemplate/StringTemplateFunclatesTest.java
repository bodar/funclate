package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Funclate;
import org.junit.Test;

import static com.googlecode.funclate.Model.model;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StringTemplateFunclatesTest {
    @Test
    public void loadsTemplates() throws Exception {
        Funclate funclate = new StringTemplateFunclate(StringTemplateFunclatesTest.class);
        String result = funclate.get("test").render(model().add("foo", "bar"));
        assertThat(result, is("bar"));
    }
}
