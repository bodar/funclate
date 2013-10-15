package com.googlecode.funclate.json;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import static com.googlecode.funclate.Model.persistent.model;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors.sortedMap;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.junit.Assert.assertThat;

public class StreamingJsonTest {
    @Test
    public void canStreamAnIterator() throws Exception {
        Sequence<String> values = sequence("1", "2");
        Writer writer = new StringWriter();

        StreamingJson.toJson(values.iterator(), writer);

        String actual = writer.toString();
        assertThat(actual, is("[\"1\",\"2\"]"));
        assertThat(actual, is(Json.toJson(values)));
    }

    @Test
    public void canStreamANestedStructure() throws Exception {
        Model model = model().
                add("root", model().
                        add("parent", model().
                                add("children", "1").add("children", true)));
        Writer writer = new StringWriter();

        StreamingJson.toJson(model, writer);

        String actual = writer.toString();
        assertThat(actual, is("{\"root\":{\"parent\":{\"children\":[\"1\",true]}}}"));
        assertThat(actual, is(Json.toJson(model)));
    }

    @Test
    public void canStreamAnIterable() throws Exception {
        Sequence<String> values = sequence("1", "2");
        Writer writer = new StringWriter();

        StreamingJson.toJson(values, writer);

        String actual = writer.toString();
        assertThat(actual, is("[\"1\",\"2\"]"));
        assertThat(actual, is(Json.toJson(values)));
    }

    @Test
    public void canStreamAMap() throws Exception {
        Map<String, Integer> values = sortedMap("one", 1, "two", 2);
        Writer writer = new StringWriter();

        StreamingJson.toJson(values, writer);

        String actual = writer.toString();
        assertThat(actual, is("{\"one\":1,\"two\":2}"));
        assertThat(actual, is(Json.toJson(values)));
    }

    @Test
    public void canStreamAMapEvenIfCastToAnIterable() throws Exception {
        Map<String, Integer> values = sortedMap("one", 1, "two", 2);
        Writer writer = new StringWriter();

        StreamingJson.toJson((Iterable)values, writer);

        String actual = writer.toString();
        assertThat(actual, is("{\"one\":1,\"two\":2}"));
        assertThat(actual, is(Json.toJson(values)));
    }
}
