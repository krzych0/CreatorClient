package com.imgtec.creator.request;

import com.imgtec.creator.response.ResponseHandler;
import com.imgtec.creator.utils.Verify;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Base class for all HTTP requests. It is generic class and it is not bound
 * to {@link com.imgtec.creator.pojo.Hateoas} as dedicated GET/PUT/POST/DELETE methods.
 * It is used by {@link com.imgtec.creator.navigation.Navigator} directly.
 */
public abstract class GenericRequest<T> {

  protected final HttpUrl url;
  protected final ResponseHandler responseHandler;

  public GenericRequest(String url, ResponseHandler responseHandler) {
    this.url = HttpUrl.parse(Verify.checkNotNull(url, "url == null"));
    this.responseHandler = Verify.checkNotNull(responseHandler, "responseHandler == null");
  }

  public HttpUrl getUrl() {
    return url;
  }

  /**
   * Prepares request to be send.
   * Default GET behaviour.
   */
  public Request prepareRequest() {
    return new Request
        .Builder()
        .url(getUrl())
        .get()
        .build();
  }

  public T execute(OkHttpClient client) throws IOException {
    Request request = prepareRequest();
    okhttp3.Response response = client.newCall(request).execute();
    return responseHandler.handle(request, response);
  }
}
