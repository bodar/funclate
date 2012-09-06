package com.googlecode.funclate.parser;

import com.googlecode.funclate.CompositeFunclate;
import com.googlecode.funclate.Funclate;
import com.googlecode.totallylazy.Callable1;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Predicates.always;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GrammarTest {
    @Test
    public void canParseAttribute() throws Exception {
        Attribute attribute = Grammar.ATTRIBUTE(new CompositeFunclate()).parse("$foo$");
        assertThat(attribute.value(), is("foo"));
    }

    @Test
    public void canParseText() throws Exception {
        Text text = Grammar.TEXT.parse("Some other text");
        assertThat(text.value(), is("Some other text"));
    }

    @Test
    public void canParseNoArgumentTemplateCall() throws Exception {
        TemplateCall noArguments = Grammar.TEMPLATE_CALL(new CompositeFunclate()).parse("$template()$");
        assertThat(noArguments.name(), is("template"));
    }

    @Test
    public void canParseTemplateCallWithNamedParameters() throws Exception {
        TemplateCall namedArguments = Grammar.TEMPLATE_CALL(new CompositeFunclate()).parse("$template(foo=bar, baz=dan)$");
        assertThat(namedArguments.name(), is("template"));
        assertThat(namedArguments.arguments().get("foo"), is("bar"));
        assertThat(namedArguments.arguments().get("baz"), is("dan"));
    }

    @Test
    public void canParseTemplateCallImplicitParameters() throws Exception {
        TemplateCall unnamed = Grammar.TEMPLATE_CALL(new CompositeFunclate()).parse("$template(foo, bar, baz)$");
        assertThat(unnamed.name(), is("template"));
        assertThat(unnamed.arguments().get("0"), is("foo"));
        assertThat(unnamed.arguments().get("1"), is("bar"));
        assertThat(unnamed.arguments().get("2"), is("baz"));
    }

    @Test
    public void canParseATemplate() throws Exception {
        Funclate funclate = new CompositeFunclate();
        funclate.add("template", always(), new Callable1<Object, String>() {
            public String call(Object o) throws Exception {
                return "Bodart";
            }
        });
        Template template = Grammar.TEMPLATE(funclate).parse("Hello $name$ $template()$");
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("name", "Dan");
        }};
        String call = template.render(map);
        assertThat(call, is("Hello Dan Bodart"));
    }
}
