package com.googlecode.funclate;

public class MutableModelTest extends ModelContract {
    @Override
    protected ModelFactory modelFactory() {
        return Model.mutable.instance;
    }
}
