package com.selonj.gson.enclosed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
public class EnclosingTypeAdapterFactoryTest {
    private Gson gson;
    private EnclosingTypeAdapterFactory factory = new EnclosingTypeAdapterFactory();

    @Before public void registerEnclosingTypeAdapterFactory() throws Exception {
        gson = new GsonBuilder().registerTypeAdapterFactory(factory).create();
        factory.setEnclosingType(User.class);
    }

    @Test public void returnNullWhenParsingNullRootObject() throws Exception {
        User user = gson.fromJson("null", User.class);
        assertThat(user, is(nullValue()));
    }

    @Test public void returnNullWhenParsingAnEmptyRootObject() throws Exception {
        User user = gson.fromJson("{}", User.class);
        assertThat(user, is(nullValue()));
    }

    @Test public void returnNewInstanceWhenParsingRootWithAnEmptyEnclosingObject() throws Exception {
        User user = gson.fromJson("{data:{}}", User.class);
        assertThat(user, is(notNullValue()));
    }

    @Test public void throwsExceptionWhenParsingEnclosedAttributeIsNotObject() throws Exception {
        try {
            gson.fromJson("{data:true}", User.class);
            fail("should raising exception");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), allOf(containsString(User.class.getName()), containsString("`data`")));
        }
    }

    @Test public void setPropertiesOnEnclosingType() throws Exception {
        User user = gson.fromJson("{data:{name:'zhangsan'}}", User.class);
        assertThat(user.name, is("zhangsan"));
    }

    @Test public void returnNullWhenMissingEnclosingAttribute() throws Exception {
        User user = gson.fromJson("{other:{name:'zhangsan'}}", User.class);
        assertThat(user, is(nullValue()));
    }

    @Test public void skipsOtherProperties() throws Exception {
        User user = gson.fromJson("{data:{name:'zhangsan'},mail:'zhangsan@163.com'}", User.class);
        assertThat(user.name, is("zhangsan"));
    }

    @Test public void returnGroupOfEnclosingObjectsWhenEnclosingAttributeIsAnArray() throws Exception {
        List<User> users = gson.fromJson("{data:[{name:'zhangsan'},{name:'lisi'}]}", List.class);
        assertThat(users, hasSize(2));
        assertThat(users.get(0).name, is("zhangsan"));
        assertThat(users.get(1).name, is("lisi"));
    }

    @Test public void supportsMultiDimensionalArrayForEnclosingObject() throws Exception {
        List<List<User>> users = gson.fromJson("{data:[[{name:'zhangsan'}]]}", List.class);
        assertThat(users, hasSize(1));
        assertThat(users.get(0), hasSize(1));
        assertThat(users.get(0).get(0).name, is("zhangsan"));
    }

    @Test public void supportsMultiLayeredEnclosingObject() throws Exception {
        User user = gson.fromJson("{other:{data:{name:'zhangsan'}}}", User.class);
        assertThat(user.name, is("zhangsan"));
    }

    @Test public void usingCustomEnclosingAttribute() throws Exception {
        factory.setEnclosingName("user");
        User user = gson.fromJson("{user:{name:'zhangsan'}}", User.class);
        assertThat(user.name, is("zhangsan"));
    }

    @Test public void returnEmptyStringWhenStringifyNull() throws Exception {
        assertThat(gson.toJson(null, User.class), equalTo(""));
    }

    @Test public void returnEmptyObjectWhenStringifyEnclosingWithNullProperties() throws Exception {
        assertThat(gson.toJson(new User(), User.class), equalTo("{\"data\":{}}"));
    }

    @Test public void stringify() throws Exception {
        String json = gson.toJson(Arrays.asList(new User("zhangsan"), new User("lisi")), List.class);
        assertThat(json, equalTo("{\"data\":[{\"name\":\"zhangsan\"},{\"name\":\"lisi\"}]}"));
    }

    @Test public void fixBugReadingMultiPropertiesInObject() throws Exception {
        User user = gson.fromJson("{other:{'id':'1',name:'zhangsan'}}", User.class);
        assertThat(user, is(nullValue()));
    }
}
