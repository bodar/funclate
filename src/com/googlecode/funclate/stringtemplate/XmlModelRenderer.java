package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.Callable1;
import org.antlr.stringtemplate.StringTemplate;

import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.URLs.packageUrl;

public class XmlModelRenderer implements Callable1<Model, String> {
    public String call(Model model) throws Exception {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(packageUrl(getClass()));
        group.registerRenderer(instanceOf(Model.class), new XmlModelRenderer());
        StringTemplate template = group.getInstanceOf("xml");
        template.setAttribute("entries", model.toMap(returnArgument()));
        return template.toString();
    }
}
