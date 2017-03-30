package com.imgtec.creator.request;

import com.imgtec.creator.pojo.Hateoas;
import com.imgtec.creator.response.ResponseHandler;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Base class for all requests.
 * @param <T> type of the object
 * @param <R> type of expected response
 */
public abstract class BaseRequest<T extends Hateoas, R extends Hateoas> extends GenericRequest {


  public BaseRequest(String url, ResponseHandler responseHandler) {
    super(url, responseHandler);
  }

  public R execute(OkHttpClient client, Class<R> clazz) throws IOException {
    Request request = prepareRequest();
    okhttp3.Response response = client.newCall(request).execute();
    return responseHandler.handle(request, response, clazz);
  }
}
