package com.imgtec.creator.response;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.pojo.Hateoas;
import com.imgtec.creator.utils.Deserializer;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * GSON specific implementation of {@link ResponseHandler}.
 */
public class GsonResponseHandler extends ResponseHandler {

  private final Deserializer deserializer;

  public GsonResponseHandler(Deserializer deserializer) {
    this.deserializer = deserializer;
  }

  @Override
  public JsonElement handle(Request request, Response response) throws IOException {

    if (response.isSuccessful()) {

      return new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .create()
          .fromJson(response.body().string(), JsonElement.class);
    }
    throw new RuntimeException("Request failed with code:" + response.code());
  }

  @Override
  public <T extends Hateoas> T handle(Request request, Response response, Class<T> returnType)
      throws IOException {

    if (response.isSuccessful()) {

      if (response.code() == 204) { //No Content
        return null;
      }

      return deserializer.fromJson(response.body().string(), returnType);
    }

    throw new RuntimeException("Request failed with code:" + response.code());
  }

  @Override
  public <T extends Hateoas> T handle(Request request, Response response, TypeToken<T> typeToken)
      throws IOException {
    if (response.isSuccessful()) {
      return deserializer.fromJson(response.body().string(), typeToken.getType());
    }
    throw new RuntimeException("Request failed with code:" + response.code());
  }
}