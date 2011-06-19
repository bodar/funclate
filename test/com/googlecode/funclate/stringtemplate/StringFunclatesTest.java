package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Funclates;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.URLs.packageUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StringFunclatesTest {
    @Test
    public void loadsTemplates() throws Exception {
        Funclates funclates = new StringFunclates(packageUrl(StringFunclatesTest.class));
        Map model = new HashMap();
        model.put("foo", "bar");
        String result = funclates.call("test", model);
        assertThat(result, is("bar"));
    }
}
