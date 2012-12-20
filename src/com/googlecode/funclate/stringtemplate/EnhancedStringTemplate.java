package com.googlecode.funclate.stringtemplate;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateWriter;
import org.antlr.stringtemplate.language.Expr;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.Executor;

import static com.googlecode.totallylazy.Strings.join;

public class EnhancedStringTemplate extends StringTemplate {
    @Override
    public String toString() {
        try {
            StringTemplateWriter writer = new NoIndentStringWriter();
            write(writer);
            return writer.toString();
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public void write(Writer out, Executor executor) throws IOException {
        setPredefinedAttributes();
        setDefaultArgumentValues();

        CharSequence result = expressions().
                mapConcurrently(toCharSequence().orElse(" "), executor).
                reduce(join);
        out.write(result.toString());
    }

    public Mapper<Expr, CharSequence> toCharSequence() {
        return new Mapper<Expr, CharSequence>() {
            @Override
            public CharSequence call(Expr expr) throws Exception {
                StringTemplateWriter writer = new NoIndentStringWriter();
                expr.write(EnhancedStringTemplate.this, writer);
                return writer.toString();
            }
        };
    }

    private Sequence<Expr> expressions() {
        return Sequences.<Object>sequence(chunks).safeCast(Expr.class);
    }

    private static class NoIndentStringWriter extends NoIndentWriter {
        public NoIndentStringWriter() {
            super(new StringWriter());
        }

        @Override
        public String toString() {
            return out.toString();
        }
    }
}