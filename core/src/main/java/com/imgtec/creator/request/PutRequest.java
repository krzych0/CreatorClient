package com.imgtec.creator.request;

import com.imgtec.creator.pojo.Hateoas;
import com.imgtec.creator.response.ResponseHandler;
import com.imgtec.creator.utils.Serializer;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Http PUT request wrapper.
 */
public class PutRequest<T extends Hateoas> extends BaseRequest<T, T> {

  private final T object;
  private final Serializer requestSerializer;

  public PutRequest(String url, T object, Serializer requestSerializer, ResponseHandler responseHandler) {
    super(url, responseHandler);
    this.object = object;
    this.requestSerializer = requestSerializer;
  }

  @Override
  public Request prepareRequest() {

    final String str = requestSerializer.toJSON(object);

    Request.Builder builder = new Request.Builder()
        .url(url)
        .put(RequestBody.create(MediaType.parse("application/json"), str));

    return builder.build();
  }
}
