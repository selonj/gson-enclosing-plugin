package test.apis;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.Ignore;
import org.junit.Test;

import java.io.StringReader;

import static com.google.gson.stream.JsonToken.END_DOCUMENT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Administrator on 2016-03-31.
 */
public class TestJsonReader {
  @Test public void readsArray() throws Exception {
    JsonReader in = fromString("[1,{},true]");

    in.beginArray();
      assertThat(in.nextInt(), is(1));
      in.beginObject();
      in.endObject();
      assertThat(in.nextBoolean(), is(true));
    in.endArray();

    assertThat("EOF",in.peek(),equalTo(END_DOCUMENT));
  }

  @Test public void readsObject() throws Exception {
    JsonReader in = fromString("{\"id\":1,\"name\":null}");

    in.beginObject();
      assertThat(in.nextName(), equalTo("id")); in.skipValue();
      assertThat(in.nextName(), equalTo("name")); in.skipValue();
    in.endObject();

    assertThat("EOF",in.peek(),equalTo(END_DOCUMENT));
  }

  @Test public void skipsAllTokens() throws Exception {
    JsonReader in = fromString("{\"id\":1,\"name\":null}");

    while (in.peek()!=END_DOCUMENT) in.skipValue();

    assertTrue(in.hasNext());
    assertThat("EOF",in.peek(),equalTo(END_DOCUMENT));
  }

  private JsonReader fromString(String json) {
    return new JsonReader(new StringReader(json));
  }
}
