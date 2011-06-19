package com.googlecode.funclate;

import org.junit.Test;

import static com.googlecode.funclate.Model.model;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ModelTest {
    @Test
    public void supportsSingleValues() throws Exception {
        Model model = model().
                add("key", "value");
        assertThat(model.get("key", String.class), is("value"));
    }

    @Test
    public void supportsMultiValues() throws Exception {
        Model model = model().
                add("key", "one").
                add("key", "two");
        assertThat(model.getValues("key", String.class), hasExactly("one", "two"));
    }

    @Test
    public void multiValuesCanBeRetrievedAsTheFirstValue() throws Exception {
        Model model = model().
                add("key", "one").
                add("key", "two");
        assertThat(model.get("key", String.class), is("one"));
    }
}
