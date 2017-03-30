package com.imgtec.creator.request;

import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.pojo.Hateoas;
import com.imgtec.creator.pojo.Instances;
import com.imgtec.creator.response.ResponseHandler;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Dedicated class for requesting IPSO instances.
 */
public class InstancesRequest<T extends Hateoas> extends BaseRequest<T, T> {

  public InstancesRequest(String url, ResponseHandler responseHandler) {
    super(url, responseHandler);
  }

  @Override
  public Request prepareRequest() {
    return new Request.Builder().url(getUrl()).get().build();
  }

  public Instances<T> execute(OkHttpClient client, TypeToken<Instances<T>> typeToken)
      throws IOException {
    Request request = prepareRequest();
    okhttp3.Response response = client.newCall(request).execute();
    return responseHandler.handle(request, response, typeToken);
  }
}