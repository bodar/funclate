package com.googlecode.funclate;

import org.junit.Test;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ModelsTest {

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
