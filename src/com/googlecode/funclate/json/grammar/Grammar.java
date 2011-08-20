package com.googlecode.funclate.json.grammar;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.numbers.Numbers;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Tuple3;

import java.math.BigDecimal;
import java.util.List;

import static org.codehaus.jparsec.Scanners.isChar;
import static org.codehaus.jparsec.Scanners.notChar;
import static org.codehaus.jparsec.Scanners.string;

public class Grammar {
    public static final Parser<Void> OPTIONAL_WHITESPACE = Scanners.WHITESPACES.optional();
    public static final Parser<Void> SEPARATOR = wsChar(',');

    private static Parser<Void> wsChar(final char character) {
        return Parsers.between(OPTIONAL_WHITESPACE, isChar(character), OPTIONAL_WHITESPACE);
    }

    public static final Parser<Void> NULL = string("null");

    public static final Parser<Boolean> BOOLEAN = string("true").or(string("false")).source().map(new Map<String, Boolean>() {
        public Boolean map(String s) {
            return Boolean.valueOf(s);
        }
    });

    public static final Parser<String> STRING = Scanners.DOUBLE_QUOTE_STRING.map(new Map<String, String>() {
        public String map(String withQuotes) {
            String substring = withQuotes.substring(1);
            return substring.substring(0, substring.length() - 1);
        }
    });

    public static final Parser<Number> NUMBER = Scanners.DECIMAL.map(new Map<String, Number>() {
        public Number map(String s) {
            return new BigDecimal(s);
        }
    });

    private static final Parser.Reference<Object> value = Parser.newReference();
    public static final Parser<Object> VALUE = value.lazy();

    public static final Parser<Pair<String, Object>> PAIR = Parsers.tuple(STRING, wsChar(':'), VALUE).map(new Map<Tuple3<String, Void, Object>, Pair<String, Object>>() {
        public Pair<String, Object> map(Tuple3<String, Void, Object> triple) {
            return Pair.pair(triple.a, triple.c);
        }
    });

    public static final Parser<List<Object>> ARRAY = Parsers.between(wsChar('['), VALUE.sepBy(SEPARATOR), wsChar(']'));

    public static final Parser<java.util.Map<String, Object>> OBJECT = Parsers.between(wsChar('{'), PAIR.sepBy(SEPARATOR), wsChar('}')).map(new Map<List<Pair<String, Object>>, java.util.Map<String, Object>>() {
        public java.util.Map<String, Object> map(List<Pair<String, Object>> pairs) {
            return Maps.map(pairs);
        }
    });

    static {
        value.set(Parsers.<Object>or(OBJECT, ARRAY, STRING, NUMBER, BOOLEAN, NULL));
    }
}
