package com.imgtec.creator.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.pojo.Hateoas;

/**
 * GSON specific implementation of {@link Deserializer}.
 */
public class GsonDeserializer implements Deserializer {

  @Override
  public <T extends Hateoas> T fromJson(String json, Class<T> returnType) {
    return new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create()
        .fromJson(json, returnType);
  }

  @Override
  public <T extends Hateoas> T fromJson(String json, TypeToken<T> typeToken) {
    return new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create()
        .fromJson(json, typeToken.getType());
  }
}
