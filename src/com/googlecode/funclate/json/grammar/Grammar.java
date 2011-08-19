package com.googlecode.funclate.json.grammar;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Tuple3;

import java.util.List;

import static org.codehaus.jparsec.Scanners.isChar;

public class Grammar {
    public static final Parser<Void> OPTIONAL_WHITESPACE = Scanners.WHITESPACES.optional();
    public static final Parser<Void> SEPARATOR = ignoreWhitespace(isChar(','));

    private static <T> Parser<T> ignoreWhitespace(Parser<T> parser) {
        return Parsers.between(OPTIONAL_WHITESPACE, parser, OPTIONAL_WHITESPACE);
    }

    public static final Parser<String> STRING = Scanners.IDENTIFIER.between(isChar('"'), isChar('"'));
    public static final Parser<Integer> INTEGER = Scanners.INTEGER.map(new Map<String, Integer>() {
        public Integer map(String s) {
            return Integer.valueOf(s);
        }
    });

    private static final Parser.Reference<Object> value = Parser.newReference();
    public static final Parser<Object> VALUE = value.lazy();

    public static final Parser<Pair<String, Object>> PAIR = Parsers.tuple(STRING, ignoreWhitespace(isChar(':')), VALUE).map(new Map<Tuple3<String, Void, Object>, Pair<String, Object>>() {
        public Pair<String, Object> map(Tuple3<String, Void, Object> triple) {
            return Pair.pair(triple.a, triple.c);
        }
    });

    public static final Parser<List<Object>> ARRAY = Parsers.between(ignoreWhitespace(isChar('[')), VALUE.sepBy(SEPARATOR), ignoreWhitespace(isChar(']')));

    public static final Parser<java.util.Map<String, Object>> OBJECT = Parsers.between(ignoreWhitespace(isChar('{')), PAIR.sepBy(SEPARATOR), ignoreWhitespace(isChar('}'))).map(new Map<List<Pair<String, Object>>, java.util.Map<String, Object>>() {
        public java.util.Map<String, Object> map(List<Pair<String, Object>> pairs) {
            return Maps.map(pairs);
        }
    });

    static {
        value.set(Parsers.<Object>or(OBJECT, ARRAY, STRING, INTEGER));
    }


}
