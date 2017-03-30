package com.imgtec.creator.auth;

import com.imgtec.creator.BaseTest;
import com.imgtec.creator.pojo.OauthToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test of {@link AuthInterceptor}.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthInterceptorTest extends BaseTest {

  @Mock
  AuthTokenProvider authTokenProviderMock;

  @Before
  public void setUp() throws Exception {
    super.setUp();
  }

  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void missingTokenProvider() throws Exception {
    try {
      new AuthInterceptor(null);
      fail("Unreachable code reached :)");
    } catch (NullPointerException e) {
      assertNotNull(null, e);
    }
  }

  @Test
  public void missingAuthTokenTest() throws Exception {

    when(authTokenProviderMock.getAuthToken()).thenReturn(null);
    mockWebServer.enqueue(new MockResponse());

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
        .addInterceptor(new AuthInterceptor(authTokenProviderMock)).build();
    okHttpClient.newCall(new Request.Builder().url(mockWebServer.url("/")).build()).execute();

    RecordedRequest request = mockWebServer.takeRequest();
    assertNull(request.getHeader(AUTHORIZATION_HEADER));
  }

  @Test
  public void authTokenTest() throws Exception {

    OauthToken token = mock(OauthToken.class);
    when(token.getTokenType()).thenReturn(TOKEN_TYPE);
    when(token.getAccessToken()).thenReturn(ACCESS_TOKEN);

    when(authTokenProviderMock.getAuthToken()).thenReturn(token);
    mockWebServer.enqueue(new MockResponse());

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
        .addInterceptor(new AuthInterceptor(authTokenProviderMock)).build();
    okHttpClient.newCall(new Request.Builder().url(mockWebServer.url("/")).build()).execute();

    RecordedRequest request = mockWebServer.takeRequest();
    assertNotNull(request.getHeader(AUTHORIZATION_HEADER));
    assertEquals(TOKEN_TYPE + " " + ACCESS_TOKEN, request.getHeader(AUTHORIZATION_HEADER));
  }
}