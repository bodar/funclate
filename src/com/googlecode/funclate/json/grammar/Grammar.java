package com.googlecode.funclate.json.grammar;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.lazyparsec.Parser;
import com.googlecode.lazyparsec.Parsers;
import com.googlecode.lazyparsec.Scanners;
import com.googlecode.lazyparsec.pattern.CharacterPredicates;
import com.googlecode.totallylazy.Triple;

import java.math.BigDecimal;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.lazyparsec.Scanners.isChar;
import static com.googlecode.lazyparsec.Scanners.string;

public class Grammar {
    public static final Parser<Void> IGNORE_WHITESPACE = isChar(CharacterPredicates.IS_WHITESPACE).skipMany();

    private static Parser<Void> wsChar(final char character) {
        return ws(isChar(character));
    }

    private static <T> Parser<T> ws(final Parser<T> parser) {
        return Parsers.between(IGNORE_WHITESPACE, parser, IGNORE_WHITESPACE);
    }

    public static final Parser<Void> NULL = string("null");

    public static final Parser<Boolean> BOOLEAN = string("true").or(string("false")).source().map(new Callable1<String, Boolean>() {
        public Boolean call(String s) {
            return Boolean.valueOf(s);
        }
    });

    public static final Parser<String> STRING = Scanners.DOUBLE_QUOTE_STRING.map(new Callable1<String, String>() {
        public String call(String withQuotes) {
            return characters(withQuotes).tail().init().toString("", "", "", Integer.MAX_VALUE);
        }
    });

    public static final Parser<Number> NUMBER = Scanners.DECIMAL.map(new Callable1<String, Number>() {
        public Number call(String s) {
            return new BigDecimal(s);
        }
    });

    private static final Parser.Reference<Object> value = Parser.newReference();
    public static final Parser<Object> VALUE = value.lazy();

    public static final Parser<Pair<String, Object>> PAIR = Parsers.tuple(STRING, wsChar(':'), VALUE).map(new Callable1<Triple<String, Void, Object>, Pair<String, Object>>() {
        public Pair<String, Object> call(Triple<String, Void, Object> triple) {
            return Pair.pair(triple.first(), triple.third());
        }
    });

    public static final Parser<Void> SEPARATOR = wsChar(',');

    public static final Parser<List<Object>> ARRAY = Parsers.between(wsChar('['), VALUE.sepBy(SEPARATOR), wsChar(']'));

    public static final Parser<java.util.Map<String, Object>> OBJECT = Parsers.between(wsChar('{'), PAIR.sepBy(SEPARATOR), wsChar('}')).map(new Callable1<List<Pair<String, Object>>, java.util.Map<String, Object>>() {
        public java.util.Map<String, Object> call(List<Pair<String, Object>> pairs) {
            return Maps.map(pairs);
        }
    });

    static {
        value.set(ws(Parsers.<Object>or(OBJECT, ARRAY, STRING, NUMBER, BOOLEAN, NULL)));
    }
}
