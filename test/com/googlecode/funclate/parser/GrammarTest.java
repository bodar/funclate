package com.googlecode.funclate.parser;

import com.googlecode.funclate.CompositeFunclate;
import com.googlecode.funclate.Funclate;
import com.googlecode.totallylazy.Callable1;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Predicates.always;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GrammarTest {
    private final Grammar grammar = new Grammar(new CompositeFunclate());

    @Test
    public void canParseAttribute() throws Exception {
        Attribute attribute = grammar.ATTRIBUTE.parse("foo");
        assertThat(attribute.value(), is("foo"));
    }

    @Test
    public void canParseText() throws Exception {
        Text text = grammar.TEXT.parse("Some other text");
        assertThat(text.value(), is("Some other text"));
    }

    @Test
    public void canParseNoArgumentTemplateCall() throws Exception {
        TemplateCall noArguments = grammar.TEMPLATE_CALL.parse("template()");
        assertThat(noArguments.name(), is("template"));
    }

    @Test
    public void canParseAnExpression() throws Exception {
        assertThat(grammar.EXPRESSION.parse("$template()$"), Matchers.instanceOf(TemplateCall.class));
        assertThat(grammar.EXPRESSION.parse("$template$"), Matchers.instanceOf(Attribute.class));
    }

    @Test
    public void canParseTemplateCallWithNamedParameters() throws Exception {
        TemplateCall namedArguments = grammar.TEMPLATE_CALL.parse("template(foo=bar, baz=dan)");
        assertThat(namedArguments.name(), is("template"));
        assertThat(((Attribute) namedArguments.arguments().get("foo")).value(), is("bar"));
        assertThat(((Attribute) namedArguments.arguments().get("baz")).value(), is("dan"));
    }

    @Test
    public void canParseTemplateCallImplicitParameters() throws Exception {
        TemplateCall unnamed = grammar.TEMPLATE_CALL.parse("template(foo, bar, baz)");
        assertThat(unnamed.name(), is("template"));
        assertThat(((Attribute) unnamed.arguments().get("0")).value(), is("foo"));
        assertThat(((Attribute) unnamed.arguments().get("1")).value(), is("bar"));
        assertThat(((Attribute) unnamed.arguments().get("2")).value(), is("baz"));
    }

    @Test
    @Ignore
    public void canParseTemplateCallLiteralParameters() throws Exception {
        TemplateCall unnamed = grammar.TEMPLATE_CALL.parse("template(\"foo\", \"bar\", \"baz\")");
        assertThat(unnamed.name(), is("template"));
        assertThat(((Text) unnamed.arguments().get("0")).value(), is("foo"));
        assertThat(((Text) unnamed.arguments().get("1")).value(), is("bar"));
        assertThat(((Text) unnamed.arguments().get("2")).value(), is("baz"));
    }

    @Test
    public void canParseATemplate() throws Exception {
        Funclate funclate = new CompositeFunclate();
        funclate.add("template", always(), new Callable1<Object, String>() {
            public String call(Object o) throws Exception {
                return "Bodart";
            }
        });
        Grammar grammar = new Grammar(funclate);
        Template template = grammar.parse("Hello $name$ $template()$");
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("name", "Dan");
        }};
        String call = template.render(map);
        assertThat(call, is("Hello Dan Bodart"));
    }
}
