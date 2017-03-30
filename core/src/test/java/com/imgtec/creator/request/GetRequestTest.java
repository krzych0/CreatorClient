package com.imgtec.creator.request;

import com.imgtec.creator.BaseTest;
import com.imgtec.creator.CreatorClient;
import com.imgtec.creator.pojo.Api;
import com.imgtec.creator.response.GsonResponseHandler;
import com.imgtec.creator.response.ResponseHandler;
import com.imgtec.creator.utils.GsonDeserializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit test for class {@link BaseRequest}.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetRequestTest extends BaseTest {

  @Mock
  CreatorClient client;
  ResponseHandler responseHandler;
  String dummyUrl = "http://dummy.url.com";
  GetRequest request;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    responseHandler = new GsonResponseHandler(new GsonDeserializer());
    request = new GetRequest<>(dummyUrl, responseHandler);
  }

  @Override
  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void createBaseRequest() throws Exception {
    assertNotNull(request);
  }

  @Test
  public void nullParams() throws Exception {
    testWithNull(null, responseHandler);
    testWithNull(dummyUrl, null);
  }

  private void testWithNull(String url, ResponseHandler handler) {
    try {
      new GetRequest<>(url, handler);
      fail();
    } catch (Exception e) {
      assertNotNull(e);
    }
  }

  @Test
  public void getUrl() throws Exception {
    assertNotNull(request.getUrl());
  }

  @Test
  public void prepareRequest() throws Exception {
    assertNotNull(request.prepareRequest());
  }

  @Test
  public void execute() throws Exception {
    try {

      OkHttpClient okHttpClient = new OkHttpClient()
          .newBuilder()
          .build();

      mockWebServer.enqueue(new MockResponse()
          .setResponseCode(200)
          .setBody(apiBodyMock));

      new GetRequest<Api>(url.toString(), responseHandler).execute(okHttpClient, Api.class);

      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void executeFail() throws Exception {
    try {

      OkHttpClient okHttpClient = new OkHttpClient()
          .newBuilder()
          .build();

      mockWebServer.enqueue(new MockResponse()
          .setResponseCode(404));

      new GetRequest<Api>(url.toString(), responseHandler).execute(okHttpClient, Api.class);

      fail();
    } catch (Exception e) {
      assertNotNull(e);
    }
  }

}