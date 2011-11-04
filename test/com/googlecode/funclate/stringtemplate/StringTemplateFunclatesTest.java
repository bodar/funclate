package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Funclates;
import org.junit.Test;

import static com.googlecode.funclate.Model.model;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StringTemplateFunclatesTest {
    @Test
    public void loadsTemplates() throws Exception {
        Funclates funclates = new StringTemplateFunclates(StringTemplateFunclatesTest.class);
        String result = funclates.get("test").render(model().add("foo", "bar"));
        assertThat(result, is("bar"));
    }
}
