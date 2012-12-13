package com.googlecode.funclate;

import com.googlecode.lazyparsec.Parser;
import com.googlecode.lazyparsec.Parsers;
import com.googlecode.lazyparsec.pattern.CharacterPredicates;

import static com.googlecode.lazyparsec.Scanners.isChar;

public class Grammars {
    public static final Parser<Void> IGNORE_WHITESPACE = isChar(CharacterPredicates.IS_WHITESPACE).skipMany();

    public static Parser<Void> wsChar(final char character) {
        return ws(isChar(character));
    }

    public static <T> Parser<T> ws(final Parser<T> parser) {
        return Parsers.between(IGNORE_WHITESPACE, parser, IGNORE_WHITESPACE);
    }
}
