package com.imgtec.creator.subscription;

import com.imgtec.creator.pojo.ResourceCreated;
import com.imgtec.creator.pojo.Subscription;
import com.imgtec.creator.pojo.Subscriptions;

import java.io.IOException;

/**
 * Interface to perform subscriptions with a Device Server via set of
 * create/read/update/delete operations exposed to the user.
 *
 * <p>Subscriptions mechanism supports different types of notifications.
 * Client can subscribe for following notifications:
 * <ul>
 * <li>{@link SubscriptionTypes#OBSERVATION}</li>
 * <li>{@link SubscriptionTypes#CLIENT_CONNECTED}</li>
 * <li>{@link SubscriptionTypes#CLIENT_UPDATED}</li>
 * <li>{@link SubscriptionTypes#CLIENT_DISCONNECTED}</li>
 * <li>{@link SubscriptionTypes#CLIENT_EXPIRED}</li>
 * </ul>
 * Additionally, {@link SubscriptionTypes#OBSERVATION}-like subscriptions can be:
 * <ul>
 *   <li>setup on observation on a LWM2M object - this will fire the supplied webhook
 *   when new object instance is created,deleted, or any resources are changed</li>
 *   <li>setup an observation on a LWM2M object instance - this will fire the supplied webhook
 *   when any of the resources within this instance are changed, deleted etc.</li>
 *   <li>Setup an observation on a LWM2M resource</li>
 * </ul>
 *
 */
public interface SubscriptionsManager {

  enum SubscriptionTypes {
    OBSERVATION("Observation"),
    CLIENT_CONNECTED("ClientConnected"),
    CLIENT_UPDATED("ClientUpdated"),
    CLIENT_DISCONNECTED("ClientDisconnected"),
    CLIENT_EXPIRED("ClientConnectionExpired");

    private final String param;

    SubscriptionTypes(String param) {
      this.param = param;
    }

    @Override
    public String toString() {
      return param;
    }
  }

  /**
   * Returns a {@link Subscriptions} object associated with this client.
   *
   * <p>For example,
   * <pre><code>
   *    Api api = client.get(url, Api.class);
   *    Subscriptions subscriptions = client.getSubscriptionsManager()
   *        .getSubscriptions(api.getLinkByRel("subscriptions").getHref());
   *
   * </code></pre>
   * @param url to be executed
   * @return subscriptions instance
   * @throws IOException if the request could not be executed due to e.g. connectivity problems
   */
  Subscriptions getSubscriptions(String url) throws IOException;

  /**
   * Creates new {@link com.imgtec.creator.pojo.Subscription} object on Device Server.
   * Depending on how deep you travers across to subscription links you may subscribe to
   * be notified about client change event (as shown below) or listen to IPSO object/resources changes.
   *
   * <p>For example,
   * <pre><code>
   *  HttpUrl url = HttpUrl.parse("https://deviceserver.creatordev.io");
   *  CreatorClient client = new CreatorClient
   *        .Builder()
   *        .setUrl(url)
   *        .setSslSocketFactory(LenientSSLSocketFactory.getSocketFactory(), new NaiveTrustManager())
   *        .build();
   *  client.getAuthManager().authorize("refresh token / key-secret pair");
   *  Api api = client.get(url.toString(), Api.class);
   *
   *  //Here is a root subscription object
   *  Subscriptions subscriptions = client
   *         .getSubscriptionsManager()
   *         .getSubscriptions(api.getLinkByRel("subscriptions").getHref());
   *  if (subscriptions == null) {
   *    return;
   *  }
   *
   *  final String webhook = "https://61dfbf7e.ngrok.io/testNotificationClientConnected";
   *  SubscriptionsManager subscriptionsManager = client.getSubscriptionsManager();
   *  Subscription s = new Subscription();
   *  s.setSubscriptionType(SubscriptionsManager.SubscriptionTypes.CLIENT_CONNECTED.toString());
   *  s.setUrl(testUrl);
   *  ResourceCreated response = subscriptionsManager.createSubscription(subscriptions, s);
   *  if (response == null) {
   *    //TODO: implement
   *  }
   *
   * </code></pre>
   *
   * @param subscriptions root object used to create new subscription (cannot be null)
   * @param subscription subscription to be registered (cannot be null)
   * @return object containing information about newly created subscription
   * @throws IOException if the request could not be executed due to e.g. connectivity problems
   */
  ResourceCreated createSubscription(Subscriptions subscriptions, Subscription subscription) throws IOException;

  /**
   * Updates {@param subscription} with new data.
   * @param subscription to be updated
   * @param data contains refreshed data
   * @throws IOException if the request could not be executed due to e.g. connectivity problems
   */
  void updateSubscription(Subscription subscription, Subscription data) throws IOException;

  /**
   * Cancel {@param subscription} and removed it from Device Server.
   * @param subscription to be removed
   * @throws IOException if the request could not be executed due to e.g. connectivity problems
   */
  void cancelSubscription(Subscription subscription) throws IOException;
}
