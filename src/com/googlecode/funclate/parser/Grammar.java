package com.googlecode.funclate.parser;

import com.googlecode.totallylazy.Callable1;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.functors.Map;

import java.util.List;

import static org.codehaus.jparsec.Scanners.isChar;
import static org.codehaus.jparsec.Scanners.notChar;

public class Grammar {
    public static final Parser<Attribute> ATTRIBUTE = Scanners.IDENTIFIER.between(isChar('$'), isChar('$')).map(new Map<String, Attribute>() {
        public Attribute map(String name) {
            return new Attribute(name);
        }
    });

    public static final Parser<Text> TEXT = notChar('$').many().source().map(new Map<String, Text>() {
        public Text map(String value) {
            return new Text(value);
        }
    });

    public static final Parser<Template> TEMPLATE = Parsers.<Callable1<java.util.Map<String, Object>, Object>>or(ATTRIBUTE, TEXT).many().map(new Map<List<Callable1<java.util.Map<String, Object>, Object>>, Template>() {
        public Template map(List<Callable1<java.util.Map<String, Object>, Object>> callables) {
            return new Template(callables);
        }
    });
}
