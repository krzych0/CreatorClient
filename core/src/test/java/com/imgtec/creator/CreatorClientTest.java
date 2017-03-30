package com.imgtec.creator;

import com.imgtec.creator.auth.AuthInterceptor;
import com.imgtec.creator.auth.AuthManagerImpl;
import com.imgtec.creator.auth.AuthTokenProvider;
import com.imgtec.creator.pojo.Api;
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

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.mockwebserver.MockResponse;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Unit test of {@link CreatorClient}.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreatorClientTest extends BaseTest {

  @Mock private ResponseHandler responseHandler;
  @Mock private OauthToken token;
  @Mock private AuthTokenProvider authTokenProviderMock;
  @Mock private AuthInterceptor authInterceptor;
  @Mock private AuthManagerImpl authenticator;

  @Before
  public void setUp() throws Exception {
    super.setUp();
  }

  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void createClient() throws Exception {
    final File cacheDirectory = new File("CacheResponse.tmp");
    final int cacheSize = 10 * 1024 * 1024; // 10 MiB
    final Cache cache = new Cache(cacheDirectory, cacheSize);
    final ResponseHandler responseHandler =  new GsonResponseHandler(new GsonDeserializer());
    CreatorClient client = new CreatorClient
        .Builder()
        .setUrl(url)
        .setCache(cache)
        .setResponseHandler(responseHandler)
        .setSslSocketFactory(
            LenientSSLSocketFactory.getSocketFactory(),
            new NaiveTrustManager())
        .build();
    assertNotNull(client);
  }

  @Test
  public void getMethodTest() throws Exception {
    final File cacheDirectory = new File("CacheResponse.tmp");
    final int cacheSize = 10 * 1024 * 1024; // 10 MiB
    final Cache cache = new Cache(cacheDirectory, cacheSize);
    final ResponseHandler responseHandler =  new GsonResponseHandler(new GsonDeserializer());
    CreatorClient client = new CreatorClient
        .Builder()
        .setUrl(url)
        .setCache(cache)
        .setResponseHandler(responseHandler)
        .setSslSocketFactory(
            LenientSSLSocketFactory.getSocketFactory(),
            new NaiveTrustManager())
        .build();

    final String API_BODY = MockResponseProvider.DATA_PROVIDER.getFile("api_mock_response.json")
        .replaceAll("%s", url.toString().replaceAll("/$", ""));

    mockWebServer.enqueue(new MockResponse()  //Api response
        .setResponseCode(200)
        .setBody(API_BODY));

    try {
      Api api = client.get(url.toString(), Api.class);
      assertNotNull(api);
    } catch (IOException e) {
      fail();
    }
  }
}