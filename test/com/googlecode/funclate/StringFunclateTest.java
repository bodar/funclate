package com.googlecode.funclate;

import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.callables.TimeReport;
import org.antlr.stringtemplate.StringTemplate;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.funclate.Model.model;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.callables.TimeCallable.time;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StringFunclateTest {
    @Test
    public void canParseTemplateIntoConstructor() throws Exception {
        TimeReport report = new TimeReport();
        assertThat(repeat(time(new Callable<String>() {
            public String call() throws Exception {
                StringFunclate funclate = new StringFunclate("Hello $name$");
                return funclate.render(model().add("name", "Dan"));
            }
        }, report)).take(1000).forAll(Predicates.is("Hello Dan")), is(true));
        System.out.println(report);
    }

    @Test
    public void canParseTemplateIntoConstructorString() throws Exception {
        TimeReport report = new TimeReport();
        assertThat(repeat(time(new Callable<String>() {
            public String call() throws Exception {
                StringTemplate funclate = new StringTemplate("Hello $name$");
                funclate.setArgumentContext(model().add("name", "Dan").toMap());
                return funclate.toString();
            }
        }, report)).take(1000).forAll(Predicates.is("Hello Dan")),is(true));
        System.out.println(report);
    }
}
