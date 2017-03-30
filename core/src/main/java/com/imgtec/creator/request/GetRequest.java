package com.imgtec.creator.request;


import com.imgtec.creator.response.ResponseHandler;
import com.imgtec.creator.pojo.Hateoas;

import okhttp3.Request;

/**
 * Http GET request wrapper.
 */
public class GetRequest<T extends Hateoas> extends BaseRequest<T, T> {

  public GetRequest(String url, ResponseHandler responseHandler) {
    super(url, responseHandler);
  }

  @Override
  public Request prepareRequest() {
    return new Request
        .Builder()
        .url(getUrl())
        .get()
        .build();
  }
}