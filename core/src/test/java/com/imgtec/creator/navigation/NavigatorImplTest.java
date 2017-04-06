package com.imgtec.creator.navigation;

import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.BaseTest;
import com.imgtec.creator.CreatorClient;
import com.imgtec.creator.LenientSSLSocketFactory;
import com.imgtec.creator.MockResponseProvider;
import com.imgtec.creator.NaiveTrustManager;
import com.imgtec.creator.pojo.Client;
import com.imgtec.creator.pojo.Configuration;
import com.imgtec.creator.pojo.Instances;
import com.imgtec.creator.response.GsonResponseHandler;
import com.imgtec.creator.utils.GsonDeserializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import okhttp3.mockwebserver.MockResponse;

import static com.imgtec.creator.MockResponseProvider.getFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Unit test for {@link Navigator}.
 */
@RunWith(MockitoJUnitRunner.class)
public class NavigatorImplTest extends BaseTest {

  private GsonResponseHandler responseHandler;
  private CreatorClient client;
  private Navigator navigator;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    responseHandler = new GsonResponseHandler(new GsonDeserializer());
    client = new CreatorClient
        .Builder()
        .setUrl(url)
        .setResponseHandler(responseHandler)
        .setSslSocketFactory(
            LenientSSLSocketFactory.getSocketFactory(),
            new NaiveTrustManager())
        .build();
    navigator = client.createNavigator(url.toString());
  }

  @Override
  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void createNavigatorWithNullTest() throws Exception {
    try {
      client.createNavigator(null);
      fail();
    } catch (NullPointerException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void createNavigatorTest() throws Exception {
    assertNotNull(navigator);
  }

  @Test
  public void flatQueryTest() throws Exception {

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(apiBodyMock));

    final String configurationMock =
        getFile("configuration_mock_response.json")
            .replaceAll("%s", url.toString().replaceAll("/$", ""));

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(configurationMock));

    try {
      navigator = client.createNavigator(url.toString());

      Configuration configuration = navigator.findByRel("configuration", Configuration.class);
      assertNotNull(configuration);
      assertEquals(1, configuration.getLinks().size());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void flatQueryWithWronRelTest() throws Exception {

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(apiBodyMock));

    try {
      navigator.findByRel("IdoNotExist", Configuration.class);
      fail();
    } catch (NavigationException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void queryWithFilterTest() throws Exception {

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(apiBodyMock));

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(MockResponseProvider.DATA_PROVIDER.getClients(url)));

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(MockResponseProvider.DATA_PROVIDER.getClient(url)));

    try {
      final String DEVICE_NAME = "TemperatureDeviceClient";
      navigator = client.createNavigator(url.toString());
      Client myClient = navigator.findByRel("clients")
          .filter(new PropertyFilter() {
            @Override
            public boolean accept(String propertyName, String propertyValue) {
              return propertyName.equals("Name") && propertyValue.equals(DEVICE_NAME);
            }
          })
          .get(Client.class, new GsonDeserializer());
      assertNotNull(myClient);
      assertEquals(5, myClient.getLinks().size());
      assertEquals(DEVICE_NAME, myClient.getName());

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void queryWithInvalidFilterTest() throws Exception {

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(apiBodyMock));

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(MockResponseProvider.DATA_PROVIDER.getClients(url)));

    try {
      final String DEVICE_NAME = "TemperatureDeviceClient";
      navigator = client.createNavigator(url.toString());
      navigator.findByRel("clients")
          .filter(new PropertyFilter() {
            @Override
            public boolean accept(String propertyName, String propertyValue) {
              return propertyName.equals("NotExistingProperty") && propertyValue.equals(DEVICE_NAME);
            }
          })
          .get(Client.class, new GsonDeserializer());

      fail();
    } catch (NavigationException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void requestInstancesTest() throws Exception {

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(apiBodyMock));

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(MockResponseProvider
            .DATA_PROVIDER.getClients(url)));


    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(MockResponseProvider
            .DATA_PROVIDER.getObjecttypes(url)));

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(MockResponseProvider
            .DATA_PROVIDER.getInstances(url)));

    navigator = client.createNavigator(url.toString());
    try {
      Instances<PointValue> value = navigator
          .findByRel("clients")
          .filter(new PropertyFilter() {
            @Override
            public boolean accept(String propertyName, String propertyValue) {
              return propertyName.equals("Name") && propertyValue.equals("TemperatureDeviceClient");
            }
          })
          .findByRel("objecttypes")
          .filter(new PropertyFilter() {
            @Override
            public boolean accept(String propertyName, String propertyValue) {
              return propertyName.equals("ObjectTypeID") && propertyValue.equals("3308");
            }
          })
          .findByRel("instances")
          .get(new TypeToken<Instances<PointValue>>() {
          }, new GsonDeserializer());

      assertNotNull(value);

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void requestInstanceTest() throws Exception {

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(apiBodyMock));

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(MockResponseProvider.DATA_PROVIDER.getClients(url)));

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(MockResponseProvider.DATA_PROVIDER.getObjecttypes(url)));


    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(MockResponseProvider.DATA_PROVIDER.getInstances(url)));

    recordResponse(new MockResponse()
        .setResponseCode(200)
        .setBody(MockResponseProvider.DATA_PROVIDER.getInstance(url)));

    navigator = client.createNavigator(url.toString());
    try {
      PointValue value = navigator
          .findByRel("clients")
          .filter(new PropertyFilter() {
            @Override
            public boolean accept(String propertyName, String propertyValue) {
              return propertyName.equals("Name") && propertyValue.equals("TemperatureDeviceClient");
            }
          })
          .findByRel("objecttypes")
          .filter(new PropertyFilter() {
            @Override
            public boolean accept(String propertyName, String propertyValue) {
              return propertyName.equals("ObjectTypeID") && propertyValue.equals("3308");
            }
          })
          .findByRel("instances")
          .filter(new PropertyFilter() {
            @Override
            public boolean accept(String propertyName, String propertyValue) {
              return propertyName.equals("InstanceID") && propertyValue.equals("0");
            }
          })
          .get(new TypeToken<PointValue>() {
          }, new GsonDeserializer());

      assertNotNull(value);

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}