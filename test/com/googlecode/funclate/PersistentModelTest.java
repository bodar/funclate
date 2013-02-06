package com.googlecode.funclate;

import com.googlecode.totallylazy.Sequences;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PersistentModelTest extends ModelContract {
    @Override
    protected ModelFactory modelFactory() {
        return Model.persistent.instance;
    }

    @Test
    public void getValuesComesInTheSameOrder() throws Exception {
        Model model = createModel().add("user", "Stu").add("user", "Dan").add("user", "Phill");
        System.out.println(Sequences.sequence(model.pairs()).toList());
        assertThat(model.getValues("user", String.class), is(Arrays.asList("Stu", "Dan", "Phill")));
    }
}
