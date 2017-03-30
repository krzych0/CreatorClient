package com.imgtec.creator.auth;

import com.imgtec.creator.pojo.OauthToken;

/**
 * Provides an authorization token wrapper that is used when communicating with a Device Server.
 */
public class AuthTokenProvider {

  private OauthToken authToken;

  public OauthToken getAuthToken() {
    return authToken;
  }

  public void setAuthToken(OauthToken token) {
    this.authToken = token;
  }
}
