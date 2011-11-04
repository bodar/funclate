package com.googlecode.funclate.parser;

import com.googlecode.funclate.BaseFunclates;
import com.googlecode.funclate.Funclates;
import com.googlecode.totallylazy.Callable1;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.always;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GrammarTest {
    @Test
    public void canParseAttribute() throws Exception {
        Attribute attribute = Grammar.ATTRIBUTE.parse("$foo$");
        assertThat(attribute.value(), is("foo"));
    }

    @Test
    public void canParseText() throws Exception {
        Text text = Grammar.TEXT.parse("Some other text");
        assertThat(text.value(), is("Some other text"));
    }

    @Test
    public void canParseTemplateCall() throws Exception {
        TemplateCall noArguments = Grammar.TEMPLATE_CALL.parse("$template()$");
        assertThat(noArguments.name(), is("template"));
        TemplateCall templateCall = Grammar.TEMPLATE_CALL.parse("$template(foo=bar)$");
        assertThat(templateCall.name(), is("template"));
        assertThat(templateCall.arguments().get("foo"), is("bar"));
    }

    @Test
    public void canParseATemplate() throws Exception {
        Template template = Grammar.TEMPLATE.parse("Hello $name$ $template()$");
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("name", "Dan");
        }};
        Funclates funclates = new BaseFunclates();
        funclates.add("template", always(), new Callable1<Object, String>() {
            public String call(Object o) throws Exception {
                return "Bodart";
            }
        });
        String call = template.render(pair(map, funclates));
        assertThat(call, is("Hello Dan Bodart"));
    }
}
