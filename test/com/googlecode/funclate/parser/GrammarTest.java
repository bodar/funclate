package com.googlecode.funclate.parser;

import com.googlecode.funclate.BaseFunclates;
import com.googlecode.funclate.Funclates;
import com.googlecode.totallylazy.Pair;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Pair.pair;
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
    public void canParseATemplate() throws Exception {
        Template template = Grammar.TEMPLATE.parse("Hello $name$!");
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("name", "Dan");
        }};
        Funclates funclates = new BaseFunclates();
        String call = template.render(pair(map, funclates));
        assertThat(call, is("Hello Dan!"));
    }
}
