package com.googlecode.funclate;

import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.funclate.Model.mutable.model;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringFunclateTest {

    @Test
    @Ignore
    public void canParseWithProperties() {
        StringFunclate funclate = new StringFunclate("Foo:$myTemplate(\"bar\")$");
        StringFunclate funclate3 = new StringFunclate("Foo:$myTemplate(myparam=dan, mysecondparam=\"foo\")$");
        StringFunclate funclate2 = new StringFunclate("Foo:$myTemplate(dan, \"foo\")$");
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
