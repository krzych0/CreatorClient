package com.imgtec.creator.utils;

import com.google.gson.GsonBuilder;
import com.imgtec.creator.pojo.Hateoas;

/**
 * GSON specific implementation of {@link Serializer}.
 */
public class GsonSerializer implements Serializer {

  @Override
  public <T extends Hateoas> String toJSON(T object) {
    return new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create()
        .toJson(object);
  }
}
