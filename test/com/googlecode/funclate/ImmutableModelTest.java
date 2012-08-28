package com.googlecode.funclate;

public class ImmutableModelTest extends ModelContract {
    @Override
    protected ModelFactory modelFactory() {
        return Model.immutable.instance;
    }
}
