package com.imgtec.creator.request;


import com.imgtec.creator.utils.Verify;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Http DELETE request wrapper.
 */
public class DeleteRequest {

  private final String url;

  public DeleteRequest(String url) {
    super();
    this.url = Verify.checkNotNull(url, "url == null");
  }

  public void execute(OkHttpClient client) throws IOException {
    Request request = new Request.Builder()
        .url(url).delete().build();

    okhttp3.Response response = client.newCall(request).execute();
    if (!response.isSuccessful()) {
      throw new RuntimeException(String.format("DELETE: %s, failed with code: %d",
          url, response.code()));
    }
  }
}
