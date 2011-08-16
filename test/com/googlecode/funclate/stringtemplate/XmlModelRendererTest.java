package com.googlecode.funclate.stringtemplate;

import com.googlecode.totallylazy.records.xml.Xml;
import org.junit.Test;

import static com.googlecode.funclate.Model.model;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class XmlModelRendererTest {
    @Test
    public void correctlyRendersAModel() throws Exception {
        XmlModelRenderer renderer = new XmlModelRenderer();
        String result = renderer.call(model().
                add("root", model().
                        add("foo", "bar").
                        add("foo", model().
                                add("baz", "lo"))));
        System.out.println("result = " + Xml.format(Xml.load(result)));
        assertThat(result, is("<root><foo>bar</foo><foo><baz>lo</baz></foo></root>"));
    }
}
