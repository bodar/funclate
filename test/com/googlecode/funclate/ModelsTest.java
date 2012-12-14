package com.googlecode.funclate;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ModelsTest {
    @Test
    public void canMixModelTypes() throws Exception {
        Model original = Model.mutable.model().
                add("users", Model.persistent.model().
                        add("user", Model.mutable.model().
                                add("name", "Dan").
                                add("tel", "34567890")).
                        add("user", Model.persistent.model().
                                add("name", "Mat").
                                add("tel", "978532")));

        Map<String, Object> root = original.toMap();
        Map<String, Object> users = cast(root.get("users"));
        List<Map<String, Object>> user = cast(users.get("user"));

        Map<String, Object> dan = user.get(0);
        MatcherAssert.assertThat((String) dan.get("name"), Matchers.is("Dan"));
        MatcherAssert.assertThat((String) dan.get("tel"), Matchers.is("34567890"));

        Map<String, Object> mat = user.get(1);
        MatcherAssert.assertThat((String) mat.get("name"), Matchers.is("Mat"));
        MatcherAssert.assertThat((String) mat.get("tel"), Matchers.is("978532"));
    }


    @Test
    public void mergeDisjunctModels() throws Exception {
        Model a = model().add("aFirst", "aFirstValue").add("aSecond", "aSecondValue");
        Model b = model().add("bFirst", "bFirstValue").add("bSecond", "bSecondValue");

        Model expected = model().add("aFirst", "aFirstValue").add("aSecond", "aSecondValue").add("bFirst", "bFirstValue").add("bSecond", "bSecondValue");

        assertThat(sequence(a, b).reduce(Models.merge()), is(expected));
    }

    @Test
    public void mergeConjunctModels() throws Exception {
        Model a = model().add("sharedKey", "aSharedValue").add("aSecond", "aSecondValue");
        Model b = model().add("sharedKey", "bSharedValue").add("bSecond", "bSecondValue");
        Model c = model().add("sharedKey", "bSharedValue");

        Model expected = model().add("sharedKey", asList("aSharedValue", "bSharedValue", "bSharedValue")).add("aSecond", "aSecondValue").add("bSecond", "bSecondValue");

        assertThat(sequence(a, b, c).reduce(Models.merge()), is(expected));
    }

    @Test
    public void mergeSubModels() throws Exception {
        Model a = model().add("sharedKey", model().add("aModel", "aSharedValue"));
        Model b = model().add("sharedKey", model().add("bModel", "bSharedValue"));

        Model expected = model().add("sharedKey", asList(model().add("aModel", "aSharedValue"), model().add("bModel", "bSharedValue")));

        assertThat(sequence(a, b).reduce(Models.merge()), is(expected));
    }

    @Test
    public void mergeExample() throws Exception {
        Model a = model().add("action", sequence(model().add("name", "aName").add("value", "aValue")).toList());
        Model b = model().add("action", sequence(model().add("name", "bName").add("value", "bValue")).toList());

        Model expected = model().add("action", sequence(model().add("name", "aName").add("value", "aValue"), model().add("name", "bName").add("value", "bValue")).toList());

        assertThat(sequence(a, b).reduce(Models.merge()), is(expected));
    }
}
