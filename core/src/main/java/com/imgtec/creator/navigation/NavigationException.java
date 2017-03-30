package com.imgtec.creator.navigation;

/**
 * Runtime type exceptions rise when using {@link Navigator}.
 */
public class NavigationException extends RuntimeException {

  public NavigationException(String message) {
    super(message);
  }

  public NavigationException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public NavigationException(Throwable throwable) {
    super(throwable);
  }
}
