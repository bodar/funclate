package com.googlecode.funclate.parser;

import com.googlecode.funclate.Funclate;
import com.googlecode.funclate.Renderer;
import com.googlecode.lazyparsec.Parser;
import com.googlecode.lazyparsec.Parsers;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Triple;

import java.util.List;
import java.util.Map;

import static com.googlecode.funclate.Grammars.wsChar;
import static com.googlecode.lazyparsec.Parsers.between;
import static com.googlecode.lazyparsec.Scanners.IDENTIFIER;
import static com.googlecode.lazyparsec.Scanners.isChar;
import static com.googlecode.lazyparsec.Scanners.notChar;
import static com.googlecode.totallylazy.Callables.toString;
import static com.googlecode.totallylazy.Pair.pair;

public class Grammar {
    private static final Parser<Void> SEPARATOR = wsChar(',');
    public static final Parser<Void> QUOTE = isChar('"');
    private final Funclate funclate;
    private final char del = '$';

    public Grammar(Funclate funclate) {
        this.funclate = funclate;
    }

    public final Parser<Attribute> ATTRIBUTE = IDENTIFIER.map(new Callable1<String, Attribute>() {
        public Attribute call(String name) {
            return new Attribute(name, funclate);
        }
    });

    public final Parser<Text> TEXT = textExcept(del);

    public final Parser<Text> LITERAL = Parsers.between(QUOTE, textExcept('"'), QUOTE);

    private Parser<Text> textExcept(char c) {
        return notChar(c).many().source().map(new Callable1<String, Text>() {
            public Text call(String value) {
                return new Text(value);
            }
        });
    }

    public final Parser<Renderer<Map<String,Object>>> VALUE = Parsers.<Renderer<Map<String, Object>>>or(LITERAL, ATTRIBUTE);
    public final Parser<Pair<String, Renderer<Map<String, Object>>>> NAMED_ARGUMENT = Parsers.tuple(IDENTIFIER, isChar('='), VALUE).map(new Mapper<Triple<String, Void, Renderer<Map<String, Object>>>, Pair<String, Renderer<Map<String, Object>>>>() {
        @Override
        public Pair<String, Renderer<Map<String, Object>>> call(Triple<String, Void, Renderer<Map<String, Object>>> triple) throws Exception {
            return pair(triple.first(), triple.third());
        }
    });

    public final Parser<Map<String, Renderer<Map<String, Object>>>> NAMED_ARGUMENTS = NAMED_ARGUMENT.sepBy1(SEPARATOR).map(new Mapper<List<Pair<String, Renderer<Map<String, Object>>>>, Map<String, Renderer<Map<String, Object>>>>() {
        public Map<String, Renderer<Map<String, Object>>> call(List<Pair<String, Renderer<Map<String, Object>>>> pairs) {
            return Maps.map(pairs);
        }
    });

    public final Parser<Map<String, Renderer<Map<String, Object>>>> IMPLICIT_ARGUMENTS = VALUE.sepBy1(SEPARATOR).map(new Mapper<List<Renderer<Map<String, Object>>>, Map<String, Renderer<Map<String, Object>>>>() {
        @Override
        public Map<String, Renderer<Map<String, Object>>> call(List<Renderer<Map<String, Object>>> renderers) throws Exception {
            return Maps.map(Sequences.sequence(renderers).zipWithIndex().map(Callables.<Number, Renderer<Map<String, Object>>, String>first(toString)));
        }
    });

    public final Parser<Map<String, Renderer<Map<String, Object>>>> NO_ARGUMENTS = Parsers.constant(Maps.<String, Renderer<Map<String, Object>>>map());

    private final Parser<Pair<String, Map<String, Renderer<Map<String, Object>>>>> templateCall = Parsers.pair(IDENTIFIER, between(isChar('('), NAMED_ARGUMENTS.or(IMPLICIT_ARGUMENTS).or(NO_ARGUMENTS), isChar(')')));

    public final Parser<TemplateCall> TEMPLATE_CALL = templateCall.map(new Callable1<Pair<String, java.util.Map<String, Renderer<Map<String, Object>>>>, TemplateCall>() {
        public TemplateCall call(Pair<String, java.util.Map<String, Renderer<Map<String, Object>>>> pair) {
            return new TemplateCall(pair.first(), pair.second(), funclate);
        }
    });
    public final Parser<Renderer<Map<String, Object>>> EXPRESSION = Parsers.<Renderer<Map<String, Object>>>or(TEMPLATE_CALL, ATTRIBUTE).between(isChar(del), isChar(del));

    public final Parser<Template> TEMPLATE = Parsers.<Renderer<Map<String, Object>>>or(EXPRESSION, TEXT).many().map(new Callable1<List<Renderer<Map<String, Object>>>, Template>() {
        public Template call(List<Renderer<Map<String, Object>>> renderers) {
            return new Template(renderers);
        }
    });

    public Template parse(CharSequence charSequence){
        return TEMPLATE.parse(charSequence);
    }
}