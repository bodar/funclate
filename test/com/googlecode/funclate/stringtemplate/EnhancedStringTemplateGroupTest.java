package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Renderer;
import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.StringTemplate;
import org.junit.Test;

import java.net.URI;
import java.util.Date;

import static com.googlecode.funclate.LinkRenderer.toLink;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.time.Dates.date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EnhancedStringTemplateGroupTest {
    @Test
    public void correctlyRemovedDoubleSlashes() throws Exception {
        String result = EnhancedStringTemplateGroup.format("jar:file:/home/dev/Projects/baron-greenback/build/artifacts/baron-greenback-dev.build.jar!/com/googlecode/barongreenback//search/list.st");
        assertThat(result, is("jar:file:/home/dev/Projects/baron-greenback/build/artifacts/baron-greenback-dev.build.jar!/com/googlecode/barongreenback/search/list.st"));
    }

    @Test
    public void canUseMultipleFormats() throws Exception {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.registerRenderer(instanceOf(URI.class), toLink());
        StringTemplate template = group.getInstanceOf("rawLink");
        template.setAttribute("URI", URI.create("http://foo/?name=bar&id=12"));
        assertThat(template.toString(), is("http://foo/?name=bar&amp;id=12"));
    }

    @Test
    public void supportsPredicatesOverridingSuperGroup() throws Exception {
        EnhancedStringTemplateGroup superGroup = new EnhancedStringTemplateGroup(getClass());
        superGroup.registerRenderer(instanceOf(URI.class), returns("superGroup called"));

        EnhancedStringTemplateGroup subGroup = new EnhancedStringTemplateGroup(getClass());
        subGroup.registerRenderer(instanceOf(URI.class), returns("subGroup called"));
        subGroup.setSuperGroup(superGroup);

        StringTemplate template = subGroup.getInstanceOf("value");
        template.setAttribute("value", URI.create("http://foo/?name=bar&id=12"));
        assertThat(template.toString(), is("subGroup called"));
    }

    @Test
    public void supportsPredicatesFromSuperGroups() throws Exception {
        EnhancedStringTemplateGroup superGroup = new EnhancedStringTemplateGroup(getClass());
        superGroup.registerRenderer(instanceOf(Date.class), returns("superGroup called"));

        EnhancedStringTemplateGroup subGroup = new EnhancedStringTemplateGroup(getClass());
        subGroup.setSuperGroup(superGroup);

        StringTemplate template = subGroup.getInstanceOf("value");
        template.setAttribute("value", date(2001, 1, 1));
        assertThat(template.toString(), is("superGroup called"));
    }

    @Test
    public void supportsOldStyleClassRegistration() throws Exception {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.registerRenderer(Date.class, oldStyleReturn("old style"));

        StringTemplate template = group.getInstanceOf("value");
        template.setAttribute("value", date(2001, 1, 1));
        assertThat(template.toString(), is("old style"));
    }


    private AttributeRenderer oldStyleReturn(final String value) {
        return new AttributeRenderer() {
            public String toString(Object o) {
                return value;
            }

            public String toString(Object o, String s) {
                return toString(o);
            }
        };
    }

    private Renderer<Object> returns(final String value) {
        return new Renderer<Object>() {
            public String render(Object instance) throws Exception {
                return value;
            }
        };
    }
}
