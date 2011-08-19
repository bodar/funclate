package com.googlecode.funclate.stringtemplate;

import org.junit.Test;

import static com.googlecode.funclate.Model.model;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JsonModelRendererTest {
    @Test
    public void correctlyRendersASingleRootElement() throws Exception {
        JsonModelRenderer renderer = new JsonModelRenderer();
        String result = renderer.call(model().
                add("root", "text"));

        assertThat(result,
                is("{'root': 'text'}"));
    }

    @Test
    public void correctlyRendersIntegersAndText() throws Exception {
        JsonModelRenderer renderer = new JsonModelRenderer();
        String result = renderer.call(model().
                add("root", model().
                        add("child", 1).
                        add("child", "text")));

        assertThat(result,
                is("{'root': {'child': [1, 'text']}}"));
    }

    @Test
    public void correctlyRendersAModel() throws Exception {
        JsonModelRenderer renderer = new JsonModelRenderer();
        String result = renderer.call(model().
                add("root", model().
                        add("foo", "bar").
                        add("foo", model().
                                add("baz", "lo"))));

        assertThat(result,
                is("{'root': {'foo': ['bar', {'baz': 'lo'}]}}"));
    }


}
