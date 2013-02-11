package com.googlecode.funclate;

import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.UnaryFunction;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringFunclateTest {

    @Test
    public void canParseWithProperties() throws Exception {
        Funclate parent = new CompositeFunclate().add("myTemplate", Predicates.always(), StringFunclate.functions.first(new UnaryFunction<String>() {
            @Override
            public String call(String s) throws Exception {
                return "Hello " + s;
            }
        }));
        StringFunclate funclate = new StringFunclate("$myTemplate(\"bar.height\")$", parent);

        Model model = model().add("dan", "Dan");
        assertThat(funclate.render(model), is("Hello bar.height"));
    }

    @Test
    public void canParseTemplateViaConstructor() throws Exception {
        StringFunclate funclate = new StringFunclate("Hello $name$");
        assertThat(funclate.render(model().add("name", "Dan")), Matchers.is("Hello Dan"));
    }

    @Test
    @Ignore
    public void supportsIfElse() throws Exception {
        Model model = model().
                add("enabled", true).
                add("name", "Dan");
        StringFunclate funclate = new StringFunclate("Hello $if(enabled, \"Stu\", name)$");
        assertThat(funclate.render(model), Matchers.is("Hello Stu"));
    }
}
