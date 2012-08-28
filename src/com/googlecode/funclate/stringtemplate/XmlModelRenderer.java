package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Model;
import com.googlecode.funclate.MutableModel;
import com.googlecode.funclate.Renderer;
import org.antlr.stringtemplate.StringTemplate;

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.URLs.packageUrl;

public class XmlModelRenderer implements Renderer<Model> {
    public String render(Model model) {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(packageUrl(getClass()));
        group.registerRenderer(instanceOf(Model.class), new XmlModelRenderer());
        StringTemplate template = group.getInstanceOf("xml");
        template.setAttribute("entries", model.entries());
        return template.toString();
    }
}
