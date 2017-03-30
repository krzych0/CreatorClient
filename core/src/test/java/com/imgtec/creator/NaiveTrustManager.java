package com.imgtec.creator;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Trust manager that accepts everything.
 */
public class NaiveTrustManager implements X509TrustManager {
  /**
   * Doesn't throw an exception, so this is how it approves a certificate.
   *
   * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], String)
   */
  @Override
  public void checkClientTrusted(X509Certificate[] cert, String authType)
      throws CertificateException {
    // do nothing
  }

  /**
   * Doesn't throw an exception, so this is how it approves a certificate.
   *
   * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], String)
   */
  @Override
  public void checkServerTrusted(X509Certificate[] cert, String authType)
      throws CertificateException {
    // do nothing
  }

  /**
   * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
   */
  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[0];
  }
}
