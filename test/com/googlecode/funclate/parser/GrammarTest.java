package com.googlecode.funclate.parser;

import com.googlecode.funclate.CompositeFunclate;
import com.googlecode.funclate.Funclate;
import com.googlecode.funclate.Renderer;
import com.googlecode.funclate.StringFunclate;
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
    public void literalCanBeSingleOrDoubleQuoted() throws Exception {
        assertThat(grammar.LITERAL.parse("\"foo\"").value(), is("foo"));
        assertThat(grammar.LITERAL.parse("'foo'").value(), is("foo"));
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
    public void canParseTemplateCallLiteralParameters() throws Exception {
        TemplateCall unnamed = grammar.TEMPLATE_CALL.parse("template(\"foo\")");
        assertThat(unnamed.name(), is("template"));
        assertThat(((Text) unnamed.arguments().get("0")).value(), is("foo"));
    }

    @Test
    public void canParseImplicits() throws Exception {
        assertThat(grammar.IMPLICIT_ARGUMENTS.parse("a").get("0"), Matchers.instanceOf(Attribute.class));
        assertThat(grammar.IMPLICIT_ARGUMENTS.parse("a,b").get("1"), Matchers.instanceOf(Attribute.class));
        assertThat(grammar.IMPLICIT_ARGUMENTS.parse("\"a\"").get("0"), Matchers.instanceOf(Text.class));
    }

    @Test
    public void canParseValue() throws Exception {
        assertThat(grammar.VALUE.parse("a"), Matchers.instanceOf(Attribute.class));
        assertThat(grammar.VALUE.parse("\"a\""), Matchers.instanceOf(Text.class));
    }

    @Test
    public void canParseLiteral() throws Exception {
        Text text = grammar.LITERAL.parse("\"Some other text\"");
        assertThat(text.value(), is("Some other text"));
    }


    @Test
    public void canParseATemplate() throws Exception {
        Funclate funclate = new CompositeFunclate();
        funclate.add("template", always(), new Callable1<Object, String>() {
            public String call(Object o) throws Exception {
                return "Bodart";
            }
        });
        funclate.add("yourLastName", always(), StringFunclate.functions.first(new Callable1<String, String>() {
            public String call(String arg) throws Exception {
                return "Your last name is " + arg;
            }
        }));
        Grammar grammar = new Grammar(funclate);
        Template template = grammar.parse("Hello $name$ $template()$ $yourLastName(template())$");
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("name", "Dan");
        }};
        String call = template.render(map);
        assertThat(call, is("Hello Dan Bodart Your last name is Bodart"));
    }
}
