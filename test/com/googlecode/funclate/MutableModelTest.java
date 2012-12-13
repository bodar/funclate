package com.googlecode.funclate;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;

public class MutableModelTest extends ModelContract {
    @Override
    protected ModelFactory modelFactory() {
        return Model.mutable.instance;
    }

    @Test
    public void supportsCopy() throws Exception {
        Model model = createModel().
                add("key", "value");
        Model copy = model.copy();
        assertThat(copy, is(not(sameInstance(model))));
        assertThat(copy, is(model));
    }
}
