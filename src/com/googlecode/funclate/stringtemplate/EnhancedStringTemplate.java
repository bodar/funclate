package com.googlecode.funclate.stringtemplate;

import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EnhancedStringTemplate extends StringTemplate {
    @Override
    public String toString() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            write(new NoIndentWriter(writer));
            writer.close();
            return stream.toString();
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}