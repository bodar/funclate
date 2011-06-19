package com.googlecode.funclate;

import org.junit.Test;

import java.util.List;
import java.util.Map;

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

    @Test
    public void canConvertToAMap() throws Exception {
        Model model = model().
                add("root", model().
                        add("child", "value"));
        Map<String,Object> map = model.toMap();
        Map<String,Object> root = (Map<String, Object>) map.get("root");
        String child = (String) root.get("child");
        assertThat(child, is("value"));
    }
}
