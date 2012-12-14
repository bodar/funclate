package com.googlecode.funclate;

public class PersistentModelTest extends ModelContract {
    @Override
    protected ModelFactory modelFactory() {
        return Model.persistent.instance;
    }
}
