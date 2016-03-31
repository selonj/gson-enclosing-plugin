package com.selonj.gson.enclosed.dsl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by L.x on 16-3-31.
 */
public interface GsonClause {
    Gson slight();

    GsonBuilder to(GsonBuilder target);
}
