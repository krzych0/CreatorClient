package com.imgtec.creator.subscription;

import com.imgtec.creator.CreatorClient;
import com.imgtec.creator.pojo.ResourceCreated;
import com.imgtec.creator.pojo.Subscription;
import com.imgtec.creator.pojo.Subscriptions;
import com.imgtec.creator.utils.Verify;

import java.io.IOException;

public class SubscriptionsManagerImpl implements SubscriptionsManager {

  private final CreatorClient client;

  public SubscriptionsManagerImpl(CreatorClient creatorClient) {
    super();
    this.client = creatorClient;
  }

  @Override
  public Subscriptions getSubscriptions(String url) throws IOException {
    return client.get(url, Subscriptions.class);
  }

  @Override
  public ResourceCreated createSubscription(Subscriptions subscriptions, Subscription subscription) throws IOException {
    Verify.checkNotNull(subscriptions, "subscriptions == null");
    Verify.checkNotNull(subscription, "subscription == null");
    return client.post(subscriptions.getLinkByRel("add").getHref(), subscription, ResourceCreated.class);
  }

  @Override
  public void updateSubscription(Subscription subscription, Subscription data) throws IOException {
    Verify.checkNotNull(subscription, "subscription == null");
    Verify.checkNotNull(data, "data == null");
    client.put(subscription.getLinkByRel("update").getHref(), data);
  }

  @Override
  public void cancelSubscription(Subscription subscription) throws IOException {
    Verify.checkNotNull(subscription, "subscription == null");
    client.delete(subscription.getLinkByRel("remove").getHref());
  }
}
