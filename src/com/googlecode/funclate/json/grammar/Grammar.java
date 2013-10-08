package com.googlecode.funclate.json.grammar;

import com.googlecode.funclate.json.Strings;
import com.googlecode.lazyparsec.Parser;
import com.googlecode.lazyparsec.Parsers;
import com.googlecode.lazyparsec.Scanners;
import com.googlecode.lazyparsec.pattern.CharacterPredicates;
import com.googlecode.totallylazy.*;

import java.math.BigDecimal;
import java.util.List;

import static com.googlecode.funclate.Grammars.ws;
import static com.googlecode.funclate.Grammars.wsChar;
import static com.googlecode.lazyparsec.Scanners.among;
import static com.googlecode.lazyparsec.Scanners.isChar;
import static com.googlecode.lazyparsec.Scanners.string;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.Integer.parseInt;

public class Grammar {

    public static final Parser<Void> NULL = string("null");

    public static final Parser<Boolean> BOOLEAN = string("true").or(string("false")).source().map(new Callable1<String, Boolean>() {
        public Boolean call(String s) {
            return Boolean.valueOf(s);
        }
    });

    public static final Predicate<Character> UNICODE_CHARACTER = CharacterPredicates.notAmong("\"\\");

    public static final Parser<String> ESCAPED_CHARACTER = isChar('\\').followedBy(among("\"\\/bfnrt").
            or(isChar('u').followedBy(isChar(CharacterPredicates.IS_HEX_DIGIT).times(4)))).source().map(Strings.functions.unescape);

    public static final Parser<String> STRING = isChar(UNICODE_CHARACTER).source().
            or(ESCAPED_CHARACTER).many().map(join()).between(isChar('"'), isChar('"'));

    private static Function1<List<String>, String> join() {
        return new Function1<List<String>, String>() {
            public String call(List<String> strings) throws Exception {
                return sequence(strings).toString("");
            }
        };
    }


    public static final Parser<Number> NUMBER = Scanners.DECIMAL.map(new Callable1<String, Number>() {
        public Number call(String value) {
            return new BigDecimal(value);
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
