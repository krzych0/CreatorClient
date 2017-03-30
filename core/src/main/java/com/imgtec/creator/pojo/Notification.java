package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification<T extends Hateoas> extends Hateoas {

  @SerializedName("TimeTriggered")
  @Expose
  private String timeTriggered;
  @SerializedName("SubscriptionType")
  @Expose
  private String subscriptionType;
  @SerializedName("Value")
  @Expose
  private T value;

  public String getTimeTriggered() {
    return timeTriggered;
  }

  public void setTimeTriggered(String timeTriggered) {
    this.timeTriggered = timeTriggered;
  }

  public String getSubscriptionType() {
    return subscriptionType;
  }

  public void setSubscriptionType(String subscriptionType) {
    this.subscriptionType = subscriptionType;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

}
