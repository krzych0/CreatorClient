package com.imgtec.creator.auth;

import com.imgtec.creator.CreatorClient;
import com.imgtec.creator.response.ResponseHandler;
import com.imgtec.creator.pojo.Api;
import com.imgtec.creator.pojo.OauthToken;
import com.imgtec.creator.request.GetRequest;
import com.imgtec.creator.request.OauthRequest;
import com.imgtec.creator.request.RefreshTokenRequest;
import com.imgtec.creator.utils.Verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Responds to an authentication challenge from Device Server.
 */
public class AuthManagerImpl implements okhttp3.Authenticator, AuthManager {


  private static final Logger logger =
      LoggerFactory.getLogger(AuthManagerImpl.class.getSimpleName());

  private final HttpUrl url;
  private final ResponseHandler responseHandler;
  private final AuthTokenProvider tokenProvider;
  private final CreatorClient client;

  /**
   * Creates an instance of {@code AuthManagerImpl}.
   * @param client url to a device server

   * @throws NullPointerException if {@param client} parameter passed is null
   */
  public AuthManagerImpl(CreatorClient client) {
    this.client = Verify.checkNotNull(client, "client == null");

    this.url = client.getUrl();
    this.responseHandler = client.getResponseHandler();
    this.tokenProvider = client.getTokenProvider();
  }

  @Override
  public Request authenticate(Route route, Response response) throws IOException {

    logger.debug("Performing authentication...");
    if (responseCount(response) >= 3) {
      logger.debug("Max attempt number reached, skipping");
      return null;
    }

    if (authorize()) {

      return response.request().newBuilder()
          .removeHeader("Authorization")
          .addHeader("Authorization", String.format("%s %s",
              tokenProvider.getAuthToken().getTokenType(),
              tokenProvider.getAuthToken().getAccessToken()))
          .build();
    }

    return null;
  }

  private int responseCount(Response response) {
    int result = 1;
    while ((response = response.priorResponse()) != null) {
      result++;
    }
    logger.debug("Attempt number: {}", result);
    return result;
  }

  private boolean authorize() {
    try {
      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      if (client.getOkHttpClient().socketFactory() != null && client.getTrustManager() != null) {
        builder.sslSocketFactory(client.getSocketFactory(), client.getTrustManager());
      }
      OkHttpClient client = builder.build();
      if (tokenProvider.getAuthToken() == null) {
        logger.warn("Missing authorization token, authorization skipped.");
        return false;
      }

      authorize(client, tokenProvider.getAuthToken().getRefreshToken());
      return true;
    } catch (final Exception e) {
      logger.warn("Authorization failed!",e);
    }

    return false;
  }

  final void authorize(OkHttpClient client, String refreshToken) throws IOException {
    Api api = new GetRequest<Api>(url.toString(), responseHandler).execute(client, Api.class);

    OauthToken oauthToken = new RefreshTokenRequest(api.getLinkByRel("authenticate").getHref(),
        refreshToken, responseHandler).execute(client, OauthToken.class);

    //this token will be used by oauth interceptor
    synchronized (tokenProvider) {
      tokenProvider.setAuthToken(oauthToken);
    }
  }

  final void authorize(OkHttpClient client, String key, String secret) throws IOException {
    Api api = new GetRequest<Api>(url.toString(), responseHandler).execute(client, Api.class);

    OauthToken oauthToken = new OauthRequest(api.getLinkByRel("authenticate").getHref(),
        key, secret, responseHandler).execute(client, OauthToken.class);

    //this token will be used by auth interceptor
    synchronized (tokenProvider) {
      tokenProvider.setAuthToken(oauthToken);
    }
  }

  /**
   * Performs authorization using key and secret.
   * @param key key associated with a user
   * @param secret secret associated with a user
   * @throws IOException thrown by a {@param client} if request fails
   */
  public final void authorize(String key, String secret) throws IOException {
    authorize(client.getOkHttpClient(), key, secret);
  }

  /**
   * Performs authorization using refresh token.
   * @param refreshToken refresh token used to obtain new token
   * @throws IOException thrown by a {@param client} if request fails
   */
  public final void authorize(String refreshToken) throws IOException {
    authorize(client.getOkHttpClient(), refreshToken);
  }
}
