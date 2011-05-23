package com.googlecode.funclate;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

public interface Templates {
    <T, R> Templates registerRenderer(Predicate<? super T> predicate, Callable1<T,R> callable);

    Template template(String name);
}
