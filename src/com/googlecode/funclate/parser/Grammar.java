package com.googlecode.funclate.parser;

import com.googlecode.funclate.Funclates;
import com.googlecode.funclate.Renderer;
import com.googlecode.lazyparsec.Parser;
import com.googlecode.lazyparsec.Parsers;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Triple;

import java.util.List;
import java.util.Map;

import static com.googlecode.lazyparsec.Scanners.*;

public class Grammar {
    public static final Parser<Attribute> ATTRIBUTE = IDENTIFIER.between(isChar('$'), isChar('$')).map(new Callable1<String, Attribute>() {
        public Attribute call(String name) {
            return new Attribute(name);
        }
    });

    public static final Parser<Pair<String, String>> ARGUMENT = Parsers.tuple(IDENTIFIER, isChar('='), IDENTIFIER).map(new Callable1<Triple<String,Void,String>, Pair<String,String>>() {
        public Pair<String, String> call(Triple<String, Void, String> triple) {
            return Pair.pair(triple.first(),triple.third());
        }
    });

    public static final Parser<Void> SEPARATOR = isChar(',');


    public static final Parser<java.util.Map<String, String>> ARGUMENTS = Parsers.between(isChar('('), ARGUMENT.sepBy(SEPARATOR), isChar(')')).map(new Callable1<List<Pair<String,String>>, Map<String,String>>() {
        public java.util.Map<String, String> call(List<Pair<String, String>> pairs) {
            return Maps.map(pairs);
        }
    });

    public static final Parser<TemplateCall> TEMPLATE_CALL = Parsers.between(isChar('$'), Parsers.pair(IDENTIFIER, ARGUMENTS), isChar('$')).map(new Callable1<Pair<String, java.util.Map<String, String>>, TemplateCall>() {
        public TemplateCall call(Pair<String, java.util.Map<String, String>> pair) {
            return new TemplateCall(pair.first(), pair.second());
        }
    });

    public static final Parser<Text> TEXT = notChar('$').many().source().map(new Callable1<String, Text>() {
        public Text call(String value) {
            return new Text(value);
        }
    });

    public static final Parser<Template> TEMPLATE = Parsers.<Renderer<Pair<java.util.Map<String, Object>, Funclates>>>or(TEMPLATE_CALL, ATTRIBUTE, TEXT).many().map(new Callable1<List<Renderer<Pair<java.util.Map<String, Object>, Funclates>>>, Template>() {
        public Template call(List<Renderer<Pair<java.util.Map<String, Object>, Funclates>>> renderers) {
            return new Template(renderers);
        }
    });
}