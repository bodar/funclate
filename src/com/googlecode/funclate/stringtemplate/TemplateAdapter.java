package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Model;
import com.googlecode.funclate.Template;
import org.antlr.stringtemplate.StringTemplate;

public class TemplateAdapter implements Template {
    private StringTemplate template;

    public TemplateAdapter(StringTemplate template) {
        this.template = template;
    }

    public String call(Model model) throws Exception {
        template.setArgumentContext(new ModelAdapter(model));
        return template.toString();
    }
}
