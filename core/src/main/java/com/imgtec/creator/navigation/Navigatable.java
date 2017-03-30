package com.imgtec.creator.navigation;

import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.pojo.Hateoas;
import com.imgtec.creator.utils.Deserializer;

import java.io.IOException;


public interface Navigatable {

  <T extends Hateoas> T findByRel(String rel, Class<T> clazz)
      throws IOException, NavigationException;

  Navigatable findByRel(String rel) throws IOException, NavigationException;

  Navigatable filter(PropertyFilter filter) throws IOException, NavigationException;

  <T extends Hateoas> T get(Class<T> clazz, Deserializer deserializer);

  <T extends Hateoas> T get(TypeToken<T> typeToken, Deserializer deserializer);
}
