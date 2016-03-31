package com.selonj.gson.enclosed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by Administrator on 2016-03-31.
 */
public class EnclosingTypeAdapterFactoryConstraintsTest {
    private static final TypeToken<? extends Object> UNUSED_TYPE = null;
    private static final Gson UNUSED_GSON = null;

    private EnclosingTypeAdapterFactory factory = new EnclosingTypeAdapterFactory();


    @Test
    public void cannotUseWhenHasNotConfigured() throws Exception {
        try {
            factory.create(UNUSED_GSON, UNUSED_TYPE);
            fail("should failed");
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void cannotWithNullEnclosingType() throws Exception {
        try {
            factory.setEnclosingType(null);
            fail("should failed");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void cannotWithNullEnclosingName() throws Exception {
        try {
            factory.setEnclosingName(null);
            fail("should failed");
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage(), equalTo("Enclosing property can't be null !"));
        }
    }
}
