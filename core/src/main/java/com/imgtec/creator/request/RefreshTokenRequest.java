package com.imgtec.creator.request;


import com.imgtec.creator.response.ResponseHandler;
import com.imgtec.creator.pojo.OauthToken;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Specific request used to obtain new authorization token using refresh token.
 */
public class RefreshTokenRequest extends BaseRequest<OauthToken, OauthToken> {

  private final String refreshToken;

  public RefreshTokenRequest(String url, String refreshToken, ResponseHandler responseHandler) {
    super(url, responseHandler);
    this.refreshToken = refreshToken;
  }

  @Override
  public Request prepareRequest() {
    return new Request.Builder()
        .url(getUrl())
        .addHeader("Accept", "application/vnd.imgtec.com.oauthtoken+json")
        .post(new FormBody.Builder()
            .addEncoded("grant_type", "refresh_token")
            .addEncoded("refresh_token", refreshToken)
            .build()) //
        .build();
  }
}
