package com.imgtec.creator.navigation;

/**
 * Used to filter Device Server responses.
 */
public interface PropertyFilter {
  /**
   * Matcher method.
   * @param propertyName property neam to be matched
   * @param propertyValue property value to be matched
   * @return true if {@param propertyName} and {@param propertyValue} matches acceptance criteria, false otherwise.
   */
  boolean accept(String propertyName, String propertyValue);
}
