package com.imgtec.creator;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Base class for core components unit testing.
 */
public class BaseTest {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String TOKEN_TYPE = "Bearer";
  public static final String ACCESS_TOKEN = "ThisIsDummyAccessToken";
  public static final String REFRESH_TOKEN = "ThisIsDummyRefreshToken";
  protected MockWebServer mockWebServer;
  protected HttpUrl url;

  protected String apiBodyMock;
  protected String authBodyMock;


  public void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
    url = mockWebServer.url("/");

    apiBodyMock = MockResponseProvider.DATA_PROVIDER.getFile("api_mock_response.json")
        .replaceAll("%s", url.toString().replaceAll("/$", ""));
    authBodyMock = MockResponseProvider.DATA_PROVIDER.getFile("auth_mock_response.json");
  }

  public void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  protected void recordResponse(MockResponse mockResponse) {
    mockWebServer.enqueue(mockResponse);

  }

}
