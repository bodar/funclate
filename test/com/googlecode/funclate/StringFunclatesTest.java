package com.googlecode.funclate;

import org.hamcrest.Matchers;
import org.junit.Test;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.totallylazy.URLs.packageUrl;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringFunclatesTest {
    @Test
    public void canLoadFromAUrl() throws Exception {
        StringFunclates funclates = new StringFunclates(packageUrl(getClass()));
        assertThat(funclates.get("hello").render(model().add("name", "Dan")), Matchers.is("Hello Dan"));
    }
}
