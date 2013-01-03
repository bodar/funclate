package com.googlecode.funclate;

import com.googlecode.totallylazy.Function2;

public class Models {
    @Deprecated
    public static Function2<Model, Model, Model> merge() {
        return Model.functions.merge;
    }
}
