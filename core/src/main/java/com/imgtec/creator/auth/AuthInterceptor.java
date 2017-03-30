package com.imgtec.creator.auth;

import com.imgtec.creator.pojo.OauthToken;
import com.imgtec.creator.utils.Verify;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Adds authorization header to a observed requests.
 */
public class AuthInterceptor implements Interceptor {

  private final AuthTokenProvider tokenProvider;

  /**
   * Creates an instance of {@code AuthInterceptor}.
   * @param tokenProvider provides an authorization token that is used when communicating with Device Server.
   *                      This parameter cannot be null.
   * @throws NullPointerException if {@code tokenProvider} is null
   */
  public AuthInterceptor(AuthTokenProvider tokenProvider) throws NullPointerException {
    this.tokenProvider = Verify.checkNotNull(tokenProvider, "tokenProvider cannot be null");
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    OauthToken token = tokenProvider.getAuthToken();
    Request.Builder builder = chain.request().newBuilder();
    if (token != null) {
      builder.addHeader("Authorization",
          String.format("%s %s", token.getTokenType(), token.getAccessToken()));

    }
    Request request = builder.build();
    return chain.proceed(request);
  }
}
