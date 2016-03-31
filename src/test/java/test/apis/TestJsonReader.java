package test.apis;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.StringReader;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2016-03-31.
 */
public class TestJsonReader {
    @Test
    public void readArray() throws Exception {
        JsonReader in = fromString("[1,{},true]");

        in.beginArray();
            assertThat(in.nextInt(),is(1));
            in.beginObject();
            in.endObject();
            assertThat(in.nextBoolean(),is(true));
        in.endArray();
    }

    private JsonReader fromString(String json) {
        return new JsonReader(new StringReader(json));
    }
}
