package com.selonj.gson.enclosed;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2016-03-31.
 */
public class EnclosingTypeAdapterFactory implements TypeAdapterFactory {
    private static final String DEFAULT_ENCLOSED_DATA_NAME = new String("data");

    private Class enclosedType;
    private String enclosedName = DEFAULT_ENCLOSED_DATA_NAME;

    public void setEnclosingType(Class enclosedType) {
        if (enclosedType == null) {
            throw new IllegalArgumentException("Enclosing type can't be null !");
        }
        this.enclosedType = enclosedType;
    }

    public void setEnclosingName(String enclosedName) {
        if (enclosedName == null) {
            throw new IllegalArgumentException("Enclosing property can't be null !");
        }
        this.enclosedName = enclosedName;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        allowToUse();
        if (supports(type.getRawType())) {
            return new EnclosingTypeAdapter<>(gson, TypeToken.get(enclosedType), this);
        }
        return null;
    }

    private void allowToUse() {
        if (enclosedType == null) {
            throw new IllegalStateException("You have not been configure enclosing adapter!");
        }
    }

    private <T> boolean supports(Class type) {
        return enclosedType.equals(type) || isArray(type);
    }

    private boolean isArray(Class type) {
        return Collection.class.isAssignableFrom(type);
    }


    private class EnclosingTypeAdapter<T> extends TypeAdapter<T> {
        private final Gson gson;
        private final TypeToken<T> type;
        private TypeAdapterFactory skippedFactory;

        public EnclosingTypeAdapter(Gson gson, TypeToken<T> type, TypeAdapterFactory skippedFactory) {
            this.gson = gson;
            this.type = type;
            this.skippedFactory = skippedFactory;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            if (value == null) {
                return;
            }
            out.beginObject();
            out.name(enclosedName);
            stringify(value, out);
            out.endObject();
        }

        private void stringify(T value, JsonWriter out) throws IOException {
            if (isArray(value.getClass())) {
                out.beginArray();
                Collection<T> group = (Collection<T>) value;
                for (T item : group) {
                    stringify(item, out);
                }
                out.endArray();
            } else {
                secondaryAdapter().write(out, value);
            }
        }

        @Override
        public T read(JsonReader in) throws IOException {
            T result = null;
            switch (in.peek()) {
                case BEGIN_OBJECT:
                    in.beginObject();
                    result = read(in);
                    in.endObject();
                    break;
                case NAME:
                    String name = in.nextName();
                    if (name.equals(enclosedName)) {
                        result = extractEnclosedDataFrom(in);
                        skipRestOfTokens(in);
                    } else {
                        result = read(in);
                    }
                    break;
                default:
                    if (in.hasNext()) {
                        in.skipValue();
                    }
            }
            return result;
        }

        private T extractEnclosedDataFrom(JsonReader in) throws IOException {
            switch (in.peek()) {
                case BEGIN_OBJECT:
                    return parseEnclosingObject(in);
                case BEGIN_ARRAY:
                    return (T) parseGroupOfEnclosingObject(in);
                default:
                    throw new IllegalArgumentException("bad enclosing json `data` for <" + type.toString() + "> !");
            }
        }

        private T parseEnclosingObject(JsonReader in) throws IOException {
            return secondaryAdapter().read(in);
        }

        private TypeAdapter<T> secondaryAdapter() {
            return gson.getDelegateAdapter(skippedFactory, type);
        }

        private List parseGroupOfEnclosingObject(JsonReader in) throws IOException {
            List group = new ArrayList();
            in.beginArray();
            while (in.hasNext()) {
                group.add(extractEnclosedDataFrom(in));
            }
            in.endArray();
            return group;
        }

        private void skipRestOfTokens(JsonReader in) throws IOException {
            while (in.hasNext())
                in.skipValue();
        }
    }

}
