package com.imgtec.creator.request;

import com.imgtec.creator.response.ResponseHandler;
import com.imgtec.creator.pojo.OauthToken;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Specific implementation of authorization request.
 */
public class OauthRequest extends BaseRequest<OauthToken, OauthToken> {

  private final String username;
  private final String password;

  public OauthRequest(String url, String username, String password, ResponseHandler responseHandler) {
    super(url, responseHandler);
    this.username = username;
    this.password = password;
  }

  @Override
  public Request prepareRequest() {
    return new Request.Builder()
        .url(getUrl())
        .addHeader("Accept", "application/vnd.imgtec.com.oauthtoken+json")
        .post(new FormBody.Builder()
            .addEncoded("grant_type", "password")
            .addEncoded("username", username)
            .addEncoded("password", password)
            .build()) //
        .build();
  }
}
