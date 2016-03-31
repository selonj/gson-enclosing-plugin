package com.selonj.gson.enclosed.dsl;

import com.google.gson.TypeAdapterFactory;

/**
 * Created by Administrator on 2016-03-31.
 */
public interface EnclosingClause {
    TypeAdapterFactory on(String property);
}
