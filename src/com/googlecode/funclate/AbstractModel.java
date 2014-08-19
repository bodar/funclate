package com.googlecode.funclate;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.multi;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class AbstractModel implements Model{
    private multi multi;
    @Override
    public Model addOptionally(final String key, final Object value) {
        if(multi == null) multi = new multi(){};
        return multi.<Model>methodOption(key, value).getOrElse(new Callable<Model>() {
            @Override
            public Model call() throws Exception {
                return add(key, value);
            }
        });
    }

    @multimethod
    public Model addOptionally(String key, Option<?> option) {
        if(option.isEmpty()) return this;
        return add(key, option.value());
    }

    @multimethod
    public Model addOptionally(String key, List<?> list) {
        if(list.isEmpty()) return this;
        return add(key, list);
    }

    @multimethod
    public Model addOptionally(String key, Map<?, ?> map) {
        if(map.isEmpty()) return this;
        return add(key, map);
    }
}
