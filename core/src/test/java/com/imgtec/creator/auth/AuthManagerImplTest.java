package com.imgtec.creator.auth;

import com.imgtec.creator.BaseTest;
import com.imgtec.creator.CreatorClient;
import com.imgtec.creator.pojo.OauthToken;
import com.imgtec.creator.response.GsonResponseHandler;
import com.imgtec.creator.response.ResponseHandler;
import com.imgtec.creator.utils.GsonDeserializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test of {@link AuthManagerImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthManagerImplTest extends BaseTest {

  ResponseHandler responseHandler;
  @Mock private OauthToken token;
  @Mock private AuthTokenProvider authTokenProviderMock;
  @Mock private CreatorClient client;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    responseHandler = new GsonResponseHandler(new GsonDeserializer());

    token = mock(OauthToken.class);
    when(token.getRefreshToken()).thenReturn(REFRESH_TOKEN);

    when(client.getUrl()).thenReturn(url);
    when(client.getResponseHandler()).thenReturn(responseHandler);
    when(client.getTokenProvider()).thenReturn(authTokenProviderMock);
    when(authTokenProviderMock.getAuthToken()).thenReturn(token);
  }

  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void createAuthenticatorWithNullParameter() throws Exception {
    try {
      new AuthManagerImpl(null);
      fail("Unreachable code reached");
    } catch (NullPointerException e) {
      assertNotNull(e);
    }
  }


  @Test
  public void createAuthenticator() throws Exception {
    try {
      Authenticator authenticator = new AuthManagerImpl(client);
      assertNotNull(authenticator);
    } catch (Exception e) {
      fail("This shouldn't happen");
    }

  }

  @Test
  public void authorize() throws Exception {

    try {
      Authenticator authenticator = new AuthManagerImpl(client);

      OkHttpClient okHttpClient = new OkHttpClient()
          .newBuilder()
          .authenticator(authenticator)
          .build();

      mockWebServer.enqueue(new MockResponse()
          .setResponseCode(401));
      mockWebServer.enqueue(new MockResponse()  //Api response
          .setResponseCode(200)
          .setBody(apiBodyMock));
      mockWebServer.enqueue(new MockResponse()  //OAuth response
          .setResponseCode(201)
          .setBody(authBodyMock));
      mockWebServer.enqueue(new MockResponse()  //response with proper OAuth header
          .setResponseCode(200));

      okHttpClient.newCall(new Request.Builder().url(mockWebServer.url("/")).build()).execute();

      RecordedRequest request = mockWebServer.takeRequest();
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

//  @Test
//  public void authorizeWithRealServer() throws Exception {
//    HttpUrl url = HttpUrl.parse("https://deviceserver.creatordev.io");
//    ResponseHandler responseHandler = new ResponseHandler();
//    AuthTokenProvider tokenProvider = new AuthTokenProvider();
//    AuthManagerImpl authenticator = new AuthManagerImpl(url, responseHandler, tokenProvider);
//    OkHttpClient okHttpClient = new OkHttpClient()
//        .newBuilder()
//        .authenticator(authenticator)
//        .sslSocketFactory(LenientSSLSocketFactory.getSocketFactory(), new NaiveTrustManager())
//        .build();
//
//    final String key = "<put test credentials here>";
//    final String secret = "<put  test credentials here>";
//
//    try {
//      authenticator.authorize(okHttpClient, key, secret);
//      assertNotNull(tokenProvider.getAuthToken());
//    } catch (IOException e) {
//      fail();
//    }
//  }

  @Test
  public void authorizeWithKeyAndSecret() throws Exception {

    try {

      AuthManagerImpl authenticator = new AuthManagerImpl(client);

      OkHttpClient okHttpClient = new OkHttpClient()
          .newBuilder()
          .authenticator(authenticator)
          .build();
      when(client.getOkHttpClient()).thenReturn(okHttpClient);
      mockWebServer.enqueue(new MockResponse()  //Api response
          .setResponseCode(200)
          .setBody(apiBodyMock));
      mockWebServer.enqueue(new MockResponse()  //OAuth response
          .setResponseCode(201)
          .setBody(authBodyMock));

      authenticator.authorize("KEY", "SECRET");

      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void authorizeWithRefreshToken() throws Exception {

    try {

      AuthManagerImpl authenticator = new AuthManagerImpl(client);
      OkHttpClient okHttpClient = new OkHttpClient()
          .newBuilder()
          .authenticator(authenticator)
          .build();

      when(client.getOkHttpClient()).thenReturn(okHttpClient);
      mockWebServer.enqueue(new MockResponse()  //Api response
          .setResponseCode(200)
          .setBody(apiBodyMock));
      mockWebServer.enqueue(new MockResponse()  //OAuth response
          .setResponseCode(201)
          .setBody(authBodyMock));

      authenticator.authorize(authTokenProviderMock.getAuthToken().getRefreshToken());

      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void authorizeWithMissingToken() throws Exception {

    try {
      AuthManagerImpl authenticator = new AuthManagerImpl(client);
      OkHttpClient okHttpClient = new OkHttpClient()
          .newBuilder()
          .authenticator(authenticator)
          .build();

      mockWebServer.enqueue(new MockResponse()
          .setResponseCode(401));

      Response response = okHttpClient.newCall(new Request.Builder().url(mockWebServer.url("/")).build()).execute();
      assertEquals(401, response.code());
    } catch (IOException e) {
      fail();
    }
  }
}