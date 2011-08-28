package com.googlecode.funclate.parser;

import com.googlecode.funclate.Funclates;
import com.googlecode.funclate.Renderer;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Tuple3;

import java.util.List;

import static org.codehaus.jparsec.Scanners.IDENTIFIER;
import static org.codehaus.jparsec.Scanners.isChar;
import static org.codehaus.jparsec.Scanners.notChar;

public class Grammar {
    public static final Parser<Attribute> ATTRIBUTE = IDENTIFIER.between(isChar('$'), isChar('$')).map(new Map<String, Attribute>() {
        public Attribute map(String name) {
            return new Attribute(name);
        }
    });

    public static final Parser<Pair<String, String>> ARGUMENT = Parsers.tuple(IDENTIFIER, isChar('='), IDENTIFIER).map(new Map<Tuple3<String, Void, String>, Pair<String, String>>() {
        public Pair<String, String> map(Tuple3<String, Void, String> triple) {
            return Pair.pair(triple.a,triple.c);
        }
    });

    public static final Parser<Void> SEPARATOR = isChar(',');


    public static final Parser<java.util.Map<String, String>> ARGUMENTS = Parsers.between(isChar('('), ARGUMENT.sepBy(SEPARATOR), isChar(')')).map(new Map<List<Pair<String, String>>, java.util.Map<String, String>>() {
        public java.util.Map<String, String> map(List<Pair<String, String>> pairs) {
            return Maps.map(pairs);
        }
    });

    public static final Parser<TemplateCall> TEMPLATE_CALL = Parsers.between(isChar('$'), Parsers.pair(IDENTIFIER, ARGUMENTS), isChar('$')).map(new Map<org.codehaus.jparsec.functors.Pair<String, java.util.Map<String, String>>, TemplateCall>() {
        public TemplateCall map(org.codehaus.jparsec.functors.Pair<String, java.util.Map<String, String>> pair) {
            return new TemplateCall(pair.a, pair.b);
        }
    });

    public static final Parser<Text> TEXT = notChar('$').many().source().map(new Map<String, Text>() {
        public Text map(String value) {
            return new Text(value);
        }
    });

    public static final Parser<Template> TEMPLATE = Parsers.<Renderer<Pair<java.util.Map<String, Object>, Funclates>>>or(TEMPLATE_CALL, ATTRIBUTE, TEXT).many().map(new Map<List<Renderer<Pair<java.util.Map<String, Object>, Funclates>>>, Template>() {
        public Template map(List<Renderer<Pair<java.util.Map<String, Object>, Funclates>>> renderers) {
            return new Template(renderers);
        }
    });
}
