package com.googlecode.funclate;

import com.googlecode.totallylazy.Arrays;
import com.googlecode.lazyrecords.Keyword;
import com.googlecode.lazyrecords.Keywords;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.googlecode.funclate.Model.model;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;

public class ModelTest {
    @Test
    public void supportsKeywords() throws Exception {
        Keyword<String> key = Keywords.keyword("key", String.class);
        Model model = model().add(key, "one");
        assertThat(model.get(key), is("one"));
        model.add(key, "two");
        assertThat(model.getValues(key), hasExactly("one", "two"));
        model.set(key, "bar");
        assertThat(model.get(key), is("bar"));
        assertThat(model.remove(key), is("bar"));
        assertThat(model.contains(key), is(false));
    }

    @Test
    public void supportsSingleValues() throws Exception {
        Model model = model().
                add("key", "value");
        assertThat(model.get("key", String.class), is("value"));
    }

    @Test
    public void supportsSet() throws Exception {
        Model model = model().add("key", "value");
        model.set("key", "foo");
        assertThat(model.get("key", String.class), is("foo"));
    }

    @Test
    public void supportsRemove() throws Exception {
        Model model = model().
                add("key", "value");
        assertThat(model.remove("key", String.class), is("value"));
    }

    @Test
    public void supportsCopy() throws Exception {
        Model model = model().
                add("key", "value");
        Model copy = model.copy();
        assertThat(copy, is(not(sameInstance(model))));
        assertThat(copy, is(model));
    }

    @Test
    public void returnsEmptyListWhenNoValues() throws Exception {
        assertThat(model().getValues("key"), is(empty()));
    }

    @Test
    public void supportsMultiValues() throws Exception {
        Model model = model().
                add("key", "one").
                add("key", "two");
        assertThat(model.getValues("key", String.class), hasExactly("one", "two"));
    }
    
    @Test
    public void supportsListMultiValues() throws Exception {
        Model model = model().
                add("key", Arrays.list("one")).
                add("key", Arrays.list("two"));
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

    @Test
    public void supportsConvertingToStringAndBack() throws Exception {
        Model original = model().
                add("users", model().
                        add("user", model().
                                add("name", "Dan").
                                add("tel", "34567890")).
                        add("user", model().
                                add("name", "Mat").
                                add("tel", "978532")));

        String serialized = original.toString();
        Model result = Model.parse(serialized);
        assertThat(result, is(original));
    }
}
