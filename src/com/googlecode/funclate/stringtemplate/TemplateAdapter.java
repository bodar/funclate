package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Template;
import com.googlecode.totallylazy.records.Record;
import org.antlr.stringtemplate.StringTemplate;

public class TemplateAdapter implements Template {
    private StringTemplate template;

    public TemplateAdapter(StringTemplate template) {
        this.template = template;
    }

    public String call(Record record) throws Exception {
        template.setArgumentContext(new RecordAdapter(record));
        return template.toString();
    }
}
