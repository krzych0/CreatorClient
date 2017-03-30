package com.imgtec.creator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Dummy implementation of ssl socket factory.
 */
public class LenientSSLSocketFactory {

  private LenientSSLSocketFactory() {
    // created to hide default constructor
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(LenientSSLSocketFactory.class.getSimpleName());


  public static SSLSocketFactory getSocketFactory() {
    try {
      TrustManager[] tm = new TrustManager[]{new NaiveTrustManager()};
      SSLContext context = SSLContext.getInstance("SSL");
      context.init(new KeyManager[0], tm, new SecureRandom());
      return context.getSocketFactory();
    } catch (NoSuchAlgorithmException e) {
      LOGGER.warn("Couldn't create SSLSocketFactory", e);
    } catch (KeyManagementException e) {
      LOGGER.warn("Couldn't create SSLSocketFactory", e);
    }
    return null;
  }
}

