package com.imgtec.creator.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test of {@link Verify}.
 */
public class VerifyTest {

  @Test
  public void checkNotNull() throws Exception {
    Object object = new Object();
    assertEquals(object, Verify.checkNotNull(object, ""));
  }

  @Test
  public void checkNull() throws Exception {
    try {
      Object object = Verify.checkNotNull(null, "object == null");
      fail();
    } catch (NullPointerException e) {
      assertNotNull(e);
    }
  }
}