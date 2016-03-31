package com.selonj.gson.enclosed.dsl;

import com.selonj.gson.enclosed.EnclosingTypeAdapterFactory;

/**
 * Created by Administrator on 2016-03-31.
 */
public class Enclosing {
    public static EnclosingClause with(Class enclosingType) {
        final EnclosingTypeAdapterFactory factory = createFactoryEnclosing(enclosingType);
        return new EnclosingClause() {
            @Override
            public GsonClause on(String property) {
                factory.setEnclosingName(property);
                return factory;
            }
        };
    }

    private static EnclosingTypeAdapterFactory createFactoryEnclosing(Class enclosingType) {
        final EnclosingTypeAdapterFactory factory = new EnclosingTypeAdapterFactory();
        factory.setEnclosingType(enclosingType);
        return factory;
    }
}
