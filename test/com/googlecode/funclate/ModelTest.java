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
        Model original = model().
                add("users", model().
                        add("user", model().
                                add("name", "Dan").
                                add("tel", "34567890")).
                        add("user", model().
                                add("name", "Mat").
                                add("tel", "978532")));

        Map<String, Object> root = original.toMap();
        Map<String, Object> users = (Map<String, Object>) root.get("users");
        List<Map<String, Object>> user = (List<Map<String, Object>>) users.get("user");

        Map<String, Object> dan = user.get(0);
        assertThat((String) dan.get("name"), is("Dan"));
        assertThat((String) dan.get("tel"), is("34567890"));

        Map<String, Object> mat = user.get(1);
        assertThat((String) mat.get("name"), is("Mat"));
        assertThat((String) mat.get("tel"), is("978532"));

        // reverse it

        Model reversed = Model.fromMap(root);
        assertThat(reversed, is(original));


    }
}
