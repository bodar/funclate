package com.googlecode.funclate.stringtemplate;

import com.googlecode.totallylazy.Xml;
import org.junit.Test;

import static com.googlecode.funclate.Model.mutable.model;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class XmlModelRendererTest {
    @Test
    public void correctlyRendersASingleRootElement() throws Exception {
        XmlModelRenderer renderer = new XmlModelRenderer();
        String result = renderer.render(model().
                add("root", "foo"));

        assertThat(Xml.format(Xml.document(result)),
                is("<root>foo</root>\n"));
    }

    @Test
    public void correctlyRendersAModel() throws Exception {
        XmlModelRenderer renderer = new XmlModelRenderer();
        String result = renderer.render(model().
                add("root", model().
                        add("foo", "bar").
                        add("foo", model().
                                add("baz", "lo"))));

        assertThat(Xml.format(Xml.document(result)),
                is(
                        "<root>\n" +
                                "    <foo>bar</foo>\n" +
                                "    <foo>\n" +
                                "        <baz>lo</baz>\n" +
                                "    </foo>\n" +
                                "</root>\n"));
    }
}
