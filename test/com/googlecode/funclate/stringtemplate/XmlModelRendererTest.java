package com.googlecode.funclate.stringtemplate;

import com.googlecode.totallylazy.records.xml.Xml;
import org.junit.Test;

import static com.googlecode.funclate.Model.model;
import static com.googlecode.totallylazy.records.xml.Xml.document;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class XmlModelRendererTest {
    @Test
    public void correctlyRendersASingleRootElement() throws Exception {
        XmlModelRenderer renderer = new XmlModelRenderer();
        String result = renderer.render(model().
                add("root", "foo"));

        assertThat(Xml.format(document(result)),
                is("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<root>foo</root>\n"));
    }
    @Test
    public void correctlyRendersAModel() throws Exception {
        XmlModelRenderer renderer = new XmlModelRenderer();
        String result = renderer.render(model().
                add("root", model().
                        add("foo", "bar").
                        add("foo", model().
                                add("baz", "lo"))));

        assertThat(Xml.format(document(result)),
                is("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<root>\n" +
                "    <foo>bar</foo>\n" +
                "    <foo>\n" +
                "        <baz>lo</baz>\n" +
                "    </foo>\n" +
                "</root>\n"));
    }
}
