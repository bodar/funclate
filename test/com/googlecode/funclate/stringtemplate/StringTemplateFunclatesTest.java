package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Funclates;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.funclate.Model.model;
import static com.googlecode.totallylazy.URLs.packageUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StringTemplateFunclatesTest {
    @Test
    public void loadsTemplates() throws Exception {
        Funclates funclates = new StringTemplateFunclates(packageUrl(StringTemplateFunclatesTest.class));
        String result = funclates.call("test", model().add("foo", "bar"));
        assertThat(result, is("bar"));
    }
}
