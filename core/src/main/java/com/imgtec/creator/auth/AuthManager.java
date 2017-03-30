package com.imgtec.creator.auth;

import java.io.IOException;

/**
 * Interface to perform authorization with a Device Server.
 */
public interface AuthManager {

  /**
   * Performs authorization using key and secret.
   * @param key key associated with a user
   * @param secret secret associated with a user
   */
  void authorize(String key, String secret) throws IOException;

  /**
   * Performs authorization using refresh token.
   * @param refreshToken refresh token used to obtain new token
   */
  void authorize(String refreshToken) throws IOException;
}
