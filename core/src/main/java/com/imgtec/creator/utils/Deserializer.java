package com.imgtec.creator.utils;

import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.pojo.Hateoas;

/**
 * Interface to deserialize json to object model.
 */
public interface Deserializer {

  <T extends Hateoas> T fromJson(String json, Class<T> returnType);

  <T extends Hateoas> T fromJson(String json, TypeToken<T> typeToken);

}
