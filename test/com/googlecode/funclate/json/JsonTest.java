package com.googlecode.funclate.json;

import org.junit.Test;

import java.util.Map;

import static com.googlecode.funclate.Model.model;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JsonTest {
    @Test
    public void correctlyParsesASingleRootElement() throws Exception {
        Map<String, Object> result = Json.parse(("{\"root\" : \"text\"}"));

        assertThat((String) result.get("root"), is("text"));
    }

    @Test
    public void correctlyRendersASingleRootElement() throws Exception {
        String result = Json.toJson(model().
                add("root", "text"));

        assertThat(result,
                is("{\"root\":\"text\"}"));
    }

    @Test
    public void correctlyRendersIntegersAndText() throws Exception {
        String result = Json.toJson(model().
                add("root", model().
                        add("child", 1).
                        add("child", "text")));

        assertThat(result,
                is("{\"root\":{\"child\":[1,\"text\"]}}"));
    }

    @Test
    public void correctlyRendersAModel() throws Exception {
        String result = Json.toJson(model().
                add("root", model().
                        add("foo", "bar").
                        add("foo", model().
                                add("baz", 1).
                                add("baz", 2))));

        assertThat(result,
                is("{\"root\":{\"foo\":[\"bar\",{\"baz\":[1,2]}]}}"));
    }

    @Test
    public void shouldPreserveNewLineCharacters() throws Exception {

        String result = Json.toJson(model().add("text", "this is \\n a test"));
        Map<String, Object> parsed = Json.parse(result);
        assertThat((String) parsed.get("text"), is("this is \\n a test"));
    }


    @Test
    public void handlesQuotedText() throws Exception {
        String result = Json.toJson(model().add("text", "He said \"Hello\" then ..."));

        assertThat(result, is("{\"text\":\"He said \\\"Hello\\\" then ...\"}"));

        Map<String, Object> parsed = Json.parse(result);
        assertThat((String)parsed.get("text"), is("He said \"Hello\" then ..."));
    }
}
