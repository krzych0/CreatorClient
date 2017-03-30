package com.imgtec.creator.utils;

/**
 * Provides verification methods.
 */
public final class Verify {

  private Verify() {}


  /**
   * Verify if the object reference is not null.
   * @param obj an object reference
   * @param errorMessage message to be used if the check fails (will be converted to a string
   *                     using {@link String#valueOf(Object)}
   * @param <T> type parametrization
   * @return non-null reference to verified object
   * @throws NullPointerException if {@code obj} is null
   */
  public static <T> T checkNotNull(T obj, Object errorMessage) throws NullPointerException {
    if (obj == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }

    return obj;
  }
}
