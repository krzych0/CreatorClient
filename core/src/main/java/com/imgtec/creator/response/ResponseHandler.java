package com.imgtec.creator.response;

import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.pojo.Hateoas;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Class responsible for building POJO from http response.
 */
public abstract class ResponseHandler {

  public <T> T handle(Request request, Response response) throws IOException {
    return null;
  }

  public abstract <T extends Hateoas> T handle(Request request, Response response, Class<T> returnType)
      throws IOException;

  public abstract <T extends Hateoas> T handle(Request request, Response response, TypeToken<T> typeToken)
      throws IOException;
}