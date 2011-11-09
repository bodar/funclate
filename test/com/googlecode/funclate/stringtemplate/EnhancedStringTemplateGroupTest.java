package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Renderer;
import com.googlecode.funclate.stringtemplate.sharedtemplates.SharedTemplateClass;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.Uri;
import org.antlr.stringtemplate.StringTemplate;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.net.URI;
import java.net.URL;
import java.util.Date;

import static com.googlecode.funclate.LinkRenderer.toLink;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.time.Dates.date;
import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class EnhancedStringTemplateGroupTest {
    @Test
    public void correctlyRemovedDoubleSlashes() throws Exception {
        String result = EnhancedStringTemplateGroup.format("jar:file:/home/dev/Projects/baron-greenback/build/artifacts/baron-greenback-dev.build.jar!/com/googlecode/barongreenback//search/list.st");
        assertThat(result, is("jar:file:/home/dev/Projects/baron-greenback/build/artifacts/baron-greenback-dev.build.jar!/com/googlecode/barongreenback/search/list.st"));
    }

    @Test
    public void canCallANamedRendererJustLikeATemplate() throws Exception {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass()).enableFormatsAsFunctions();
        group.registerRenderer("link", instanceOf(URI.class), toLink());

        StringTemplate linkTemplate = group.getInstanceOf("linkFormat");
        linkTemplate.setAttribute("URI", URI.create("http://foo/?name=bar&id=12"));
        assertThat(linkTemplate.toString(), is("<a href=\"http://foo/?name=bar&id=12\">http://foo/?name=bar&id=12</a>"));
    }

    @Test
    public void ignoresCaseAndTrimsWhitespace() throws Exception {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.registerRenderer(" link ", instanceOf(URI.class), toLink());

        StringTemplate linkTemplate = group.getInstanceOf("customFormat");
        linkTemplate.setAttribute("URI", URI.create("http://foo/?name=bar&id=12"));
        linkTemplate.setAttribute("formatToUse", "     LINK       ");
        assertThat(linkTemplate.toString(), is("<a href=\"http://foo/?name=bar&id=12\">http://foo/?name=bar&id=12</a>"));
    }

    @Test
    public void supportsCustomFormats() throws Exception {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.registerRenderer("link", instanceOf(URI.class), toLink());
        group.registerRenderer("plain", always(), Callables.asString());
        group.registerRenderer("uppercase", instanceOf(String.class), Strings.toUpperCase());

        StringTemplate linkTemplate = group.getInstanceOf("customFormat");
        linkTemplate.setAttribute("URI", URI.create("http://foo/?name=bar&id=12"));
        linkTemplate.setAttribute("formatToUse", "link");
        assertThat(linkTemplate.toString(), is("<a href=\"http://foo/?name=bar&id=12\">http://foo/?name=bar&id=12</a>"));

        StringTemplate plainTemplate = group.getInstanceOf("customFormat");
        plainTemplate.setAttribute("URI", URI.create("http://foo/?name=bar&id=12"));
        plainTemplate.setAttribute("formatToUse", "plain");
        assertThat(plainTemplate.toString(), is("http://foo/?name=bar&id=12"));

        StringTemplate uppercaseTemplate = group.getInstanceOf("customFormat");
        uppercaseTemplate.setAttribute("URI", URI.create("http://foo/?name=bar&id=12"));
        uppercaseTemplate.setAttribute("formatToUse", "plain,uppercase");
        assertThat(uppercaseTemplate.toString(), is("HTTP://FOO/?NAME=BAR&ID=12"));
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

        EnhancedStringTemplateGroup subGroup = new EnhancedStringTemplateGroup(getClass(), superGroup);
        subGroup.registerRenderer(instanceOf(URI.class), returns("subGroup called"));

        StringTemplate template = subGroup.getInstanceOf("value");
        template.setAttribute("value", URI.create("http://foo/?name=bar&id=12"));
        assertThat(template.toString(), is("subGroup called"));
    }

    @Test
    public void supportsPredicatesFromSuperGroups() throws Exception {
        EnhancedStringTemplateGroup superGroup = new EnhancedStringTemplateGroup(getClass());
        superGroup.registerRenderer(instanceOf(Date.class), returns("superGroup called"));

        EnhancedStringTemplateGroup subGroup = new EnhancedStringTemplateGroup(getClass(), superGroup);

        StringTemplate template = subGroup.getInstanceOf("value");
        template.setAttribute("value", date(2001, 1, 1));
        assertThat(template.toString(), is("superGroup called"));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void doesNotSupportsOldStyleClassRegistration() throws Exception {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        try {
            group.registerRenderer(Date.class, null);
            fail("Should never get here");
        } catch (IllegalArgumentException e){
            assertThat(e.getMessage(), is("Please call 'registerRenderer(instanceOf(Date.class), renderer)' or 'registerRenderer(instanceOf(Date.class), 'format', renderer)'"));
        }
    }

    @Test
    public void supportsTemplatesFromParentGroup() throws Exception {
        EnhancedStringTemplateGroup shared = new EnhancedStringTemplateGroup(SharedTemplateClass.class);
        
        EnhancedStringTemplateGroup templateGroup = new EnhancedStringTemplateGroup(getClass(), shared);

        assertThat(templateGroup.getInstanceOf("test"), is(notNullValue()));
        assertThat(templateGroup.getInstanceOf("sharedTemplate"), is(notNullValue()));

    }

    private Renderer<Object> returns(final String value) {
        return new Renderer<Object>() {
            public String render(Object instance) throws Exception {
                return value;
            }
        };
    }
}
