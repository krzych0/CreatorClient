package com.imgtec.creator.subscription;

import com.imgtec.creator.BaseTest;
import com.imgtec.creator.CreatorClient;
import com.imgtec.creator.LenientSSLSocketFactory;
import com.imgtec.creator.MockResponseProvider;
import com.imgtec.creator.NaiveTrustManager;
import com.imgtec.creator.pojo.Link;
import com.imgtec.creator.pojo.ResourceCreated;
import com.imgtec.creator.pojo.Subscription;
import com.imgtec.creator.pojo.Subscriptions;
import com.imgtec.creator.response.GsonResponseHandler;
import com.imgtec.creator.utils.GsonDeserializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link SubscriptionsManager}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SubscriptionsManagerImplTest extends BaseTest {

  private GsonResponseHandler responseHandler;
  private CreatorClient client;

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

  }

  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void getSubscriptions() throws Exception {
    final String BODY = MockResponseProvider.DATA_PROVIDER.getFile("subscriptions_mock_response.json")
        .replaceAll("%s", url.toString().replaceAll("/$", ""));

    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(201)
        .setBody(BODY));

    try {
      SubscriptionsManagerImpl manager = new SubscriptionsManagerImpl(client);
      Subscriptions subscriptions = manager.getSubscriptions(url.toString());
      assertNotNull(subscriptions);
      assertNotNull(subscriptions.getItems());
      assertEquals(9, subscriptions.getItems().size());
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void createSubscription() throws Exception {
    final String SUBSCRIPTION = MockResponseProvider
        .DATA_PROVIDER.getFile("subscription_for_instance_0_mock_response.json")
        .replaceAll("%s", url.toString().replaceAll("/$", ""));

    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(201)
        .setBody(SUBSCRIPTION));

    final String RESPONSE = MockResponseProvider.DATA_PROVIDER.getFile("create_subscription_mock_response.json")
        .replaceAll("%s", url.toString().replaceAll("/$", ""));
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(201)
        .setBody(RESPONSE));

    try {
      SubscriptionsManagerImpl manager = new SubscriptionsManagerImpl(client);
      Subscriptions subscriptions = manager.getSubscriptions(url.toString());

      final String testWebhook = "https://61dfbf7e.ngrok.io/testNotificationClientConnected";
      Subscription subscription = new Subscription();
      subscription.setSubscriptionType(SubscriptionsManager.SubscriptionTypes.CLIENT_CONNECTED.toString());
      subscription.setUrl(testWebhook);

      ResourceCreated response = manager.createSubscription(subscriptions, subscription);
      assertNotNull(response);

    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void createSubscriptionWithConflict() throws Exception {

    mockWebServer.enqueue(new MockResponse().setResponseCode(409));

    try {
      SubscriptionsManagerImpl manager = new SubscriptionsManagerImpl(client);
      Subscriptions subscriptions = manager.getSubscriptions(url.toString());

      final String testWebhook = "https://61dfbf7e.ngrok.io/testNotificationClientConnected";
      Subscription subscription = new Subscription();
      subscription.setSubscriptionType(SubscriptionsManager.SubscriptionTypes.CLIENT_CONNECTED.toString());
      subscription.setUrl(testWebhook);

      manager.createSubscription(subscriptions, subscription);
      fail();
    } catch (Exception e) {
      assertNotNull(e);
    }
  }

  @Test
  public void updateSubscription() throws Exception {

  }

  @Test
  public void cancelSubscription() throws Exception {
    mockWebServer.enqueue(new MockResponse().setResponseCode(204)); //No Content

    try {
      SubscriptionsManagerImpl manager = new SubscriptionsManagerImpl(client);
      Subscription subscription = mock(Subscription.class);
      Link link = mock(Link.class);
      when(link.getHref()).thenReturn(url.toString());
      when(subscription.getLinkByRel("remove")).thenReturn(link);

      manager.cancelSubscription(subscription);
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }
}