package exmaples;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.selonj.gson.enclosed.dsl.Enclosing;
import com.selonj.gson.enclosed.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2016-03-31.
 */
public class EnclosingExample {
    private Gson gson;

    @Before
    public void registerEnclosingAdapter() throws Exception {
        gson = new GsonBuilder().registerTypeAdapterFactory(Enclosing.with(User.class).on("user")).create();
    }

    @Test
    public void extractEnclosedObjectFromJson() throws Exception {
        User user = gson.fromJson("{user:{name:'zhangsan',mail:'zhangsan@163.com'}}", User.class);

        assertThat(user.name, equalTo("zhangsan"));
        assertThat(user.mail, equalTo("zhangsan@163.com"));
    }

    @Test
    public void extractGroupOfEnclosedObjectsFromJson() throws Exception {
        List<User> users = gson.fromJson("{user:[{name:'zhangsan'},{name:'lisi'}]}", List.class);

        assertThat(users, hasSize(2));
        assertThat(users.get(0).name, equalTo("zhangsan"));
        assertThat(users.get(1).name, equalTo("lisi"));
    }

    @Test
    public void stringifyEnclosedObject() throws Exception {
        String json = gson.toJson(new User("zhangsan", "zhangsan@163.com"), User.class);
        assertThat(json, equalTo("{\"user\":{\"name\":\"zhangsan\",\"mail\":\"zhangsan@163.com\"}}"));
    }

    @Test
    public void stringifyGroupOfEnclosedObjects() throws Exception {
        String json = gson.toJson(Arrays.asList(new User("zhangsan"), new User("lisi")), List.class);
        assertThat(json, equalTo("{\"user\":[{\"name\":\"zhangsan\"},{\"name\":\"lisi\"}]}"));
    }
}
