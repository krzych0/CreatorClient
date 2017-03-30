package com.imgtec.creator.request;

import com.imgtec.creator.pojo.Hateoas;
import com.imgtec.creator.response.ResponseHandler;
import com.imgtec.creator.utils.Serializer;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Http POST request wrapper.
 */
public class PostRequest<T extends Hateoas, R extends Hateoas> extends BaseRequest<T, R> {

  private final T object;
  private final Serializer requestSerializer;

  public PostRequest(String url, T object, Serializer requestSerializer, ResponseHandler responseHandler) {
    super(url, responseHandler);
    this.object = object;
    this.requestSerializer = requestSerializer;
  }

  @Override
  public Request prepareRequest() {
    return new Request.Builder()
        .url(getUrl())
        .post(RequestBody
                .create(MediaType.parse("application/json"), requestSerializer.toJSON(object)))
        .build();
  }
}
