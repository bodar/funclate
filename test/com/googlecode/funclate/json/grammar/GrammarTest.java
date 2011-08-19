package com.googlecode.funclate.json.grammar;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GrammarTest {
    @Test
    public void canParseString() throws Exception {
        String string = Grammar.STRING.parse("\"foo\"");
        assertThat(string, is("foo"));
    }

    @Test
    public void canParseNumber() throws Exception {
        Integer number = Grammar.INTEGER.parse("12");
        assertThat(number, is(12));
    }

    @Test
    public void canParsePair() throws Exception {
        Pair<String, Object> pair = Grammar.PAIR.parse("\"foo\":\"value\"");
        assertThat(pair.first(), is("foo"));
        assertThat((String) pair.second(), is("value"));
        Pair<String, Object> parse = Grammar.PAIR.parse("\"foo\":123");
        assertThat(parse.first(), is("foo"));
        assertThat((Integer) parse.second(), is(123));
    }

    @Test
    public void canParseArray() throws Exception {
        List<Object> listOfOne = Grammar.ARRAY.parse("[\"foo\"]");
        assertThat((String) listOfOne.get(0), is("foo"));
        List<Object> listOfTwo = Grammar.ARRAY.parse("[\"foo\",123]");
        assertThat((String) listOfTwo.get(0), is("foo"));
        assertThat((Integer) listOfTwo.get(1), is(123));
    }

    @Test
    public void canParseObjectLiteral() throws Exception {
        Map<String, Object> mapOfOne = Grammar.OBJECT.parse("{\"foo\":123}");
        assertThat((Integer) mapOfOne.get("foo"), is(123));
        Map<String, Object> mapOfTwo = Grammar.OBJECT.parse("{\"foo\":123,\"bar\":\"baz\"}");
        assertThat((Integer) mapOfTwo.get("foo"), is(123));
        assertThat((String) mapOfTwo.get("bar"), is("baz"));
    }

    @Test
    public void canParseAValue() throws Exception {
        Object number = Grammar.VALUE.parse("1");
        assertThat(number, is((Object)1));
        Object string = Grammar.VALUE.parse("\"foo\"");
        assertThat(string, is((Object) "foo"));
        Object map = Grammar.VALUE.parse("{\"foo\":123}");
        assertThat(((Map)map).get("foo"), is((Object) 123));
        Object array = Grammar.VALUE.parse("[\"foo\",123]");
        assertThat(((List)array).get(0), is((Object) "foo"));
        assertThat(((List)array).get(1), is((Object) 123));
    }

    @Test
    public void canParseNestedJson() throws Exception{
        Map map = (Map) Grammar.VALUE.parse(" { \"root\"  : { \"foo\" : [ \"bar\", { \"baz\" : [ 1 , \"foo\"] } ] } }  ");
        Map root = (Map) map.get("root");
        List foo = (List) root.get("foo");
        assertThat((String) foo.get(0), is("bar"));
    }



}
