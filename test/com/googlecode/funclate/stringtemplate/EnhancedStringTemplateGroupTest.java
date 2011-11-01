package com.googlecode.funclate.stringtemplate;

import org.antlr.stringtemplate.StringTemplate;
import org.junit.Test;

import java.net.URI;

import static com.googlecode.funclate.LinkRenderer.toLink;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EnhancedStringTemplateGroupTest {
    @Test
    public void correctlyRemovedDoubleSlashes() throws Exception {
        String result = EnhancedStringTemplateGroup.format("jar:file:/home/dev/Projects/baron-greenback/build/artifacts/baron-greenback-dev.build.jar!/com/googlecode/barongreenback//search/list.st");
        assertThat(result, is("jar:file:/home/dev/Projects/baron-greenback/build/artifacts/baron-greenback-dev.build.jar!/com/googlecode/barongreenback/search/list.st"));
    }

    @Test
    public void canUseMultipleFormats() throws Exception{
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.registerRenderer(instanceOf(URI.class), toLink());
        StringTemplate template = group.getInstanceOf("rawLink");
        template.setAttribute("URI", URI.create("http://foo/?name=bar&id=12"));
        assertThat(template.toString(), is("http://foo/?name=bar&amp;id=12"));
    }
}
