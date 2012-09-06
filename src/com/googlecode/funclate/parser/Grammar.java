package com.googlecode.funclate.parser;

import com.googlecode.funclate.Funclate;
import com.googlecode.funclate.Renderer;
import com.googlecode.lazyparsec.Parser;
import com.googlecode.lazyparsec.Parsers;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.Triple;

import java.util.List;
import java.util.Map;

import static com.googlecode.funclate.Grammars.wsChar;
import static com.googlecode.lazyparsec.Parsers.between;
import static com.googlecode.lazyparsec.Parsers.sequence;
import static com.googlecode.lazyparsec.Scanners.IDENTIFIER;
import static com.googlecode.lazyparsec.Scanners.isChar;
import static com.googlecode.lazyparsec.Scanners.notChar;
import static com.googlecode.lazyparsec.Scanners.string;
import static com.googlecode.totallylazy.Callables.toString;

public class Grammar {
    private static final Parser<String> attribute = IDENTIFIER.between(isChar('$'), isChar('$'));

    public static Parser<Attribute> ATTRIBUTE(final Funclate funclate) {
        return attribute.map(new Callable1<String, Attribute>() {
            public Attribute call(String name) {
                return new Attribute(name, funclate);
            }
        });
    }

    public static final Parser<Pair<String, String>> NAMED_ARGUMENT = Parsers.tuple(IDENTIFIER, isChar('='), IDENTIFIER).map(new Callable1<Triple<String, Void, String>, Pair<String, String>>() {
        public Pair<String, String> call(Triple<String, Void, String> triple) {
            return Pair.pair(triple.first(), triple.third());
        }
    });

    public static final Parser<Void> SEPARATOR = wsChar(',');


    public static final Parser<Map<String, String>> NAMED_ARGUMENTS = NAMED_ARGUMENT.sepBy(SEPARATOR).map(new Callable1<List<Pair<String, String>>, Map<String, String>>() {
        public Map<String, String> call(List<Pair<String, String>> pairs) {
            return Maps.map(pairs);
        }
    });

    private static final Parser<Map<String, String>> IMPLICIT_ARGUMENTS = IDENTIFIER.sepBy(SEPARATOR).map(new Callable1<List<String>, Map<String, String>>() {
        @Override
        public Map<String, String> call(List<String> strings) throws Exception {
            return Maps.map(Sequences.sequence(strings).zipWithIndex().map(Callables.<Number, String, String>first(toString)));
        }
    }) ;

    private static final Parser<Pair<String, Map<String, String>>> templateCall = between(isChar('$'), Parsers.pair(IDENTIFIER,  between(isChar('('), NAMED_ARGUMENTS.or(IMPLICIT_ARGUMENTS), isChar(')'))), isChar('$'));

    public static Parser<TemplateCall> TEMPLATE_CALL(final Funclate funclate) {
        return templateCall.map(new Callable1<Pair<String, java.util.Map<String, String>>, TemplateCall>() {
            public TemplateCall call(Pair<String, java.util.Map<String, String>> pair) {
                return new TemplateCall(pair.first(), pair.second(), funclate);
            }
        });
    }

    public static final Parser<Text> TEXT = notChar('$').many().source().map(new Callable1<String, Text>() {
        public Text call(String value) {
            return new Text(value);
        }
    });

    public static Parser<Template> TEMPLATE(Funclate funclate) {
        return Parsers.<Renderer<Map<String, Object>>>or(TEMPLATE_CALL(funclate), ATTRIBUTE(funclate), TEXT).many().map(new Callable1<List<Renderer<Map<String, Object>>>, Template>() {
            public Template call(List<Renderer<Map<String, Object>>> renderers) {
                return new Template(renderers);
            }
        });
    }
}