package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscription extends Hateoas {

  @SerializedName("SubscriptionType")
  @Expose
  private String subscriptionType;

  @SerializedName("AcceptContentType")
  @Expose
  private String acceptContentType;

  @SerializedName("Url")
  @Expose
  private String url;

  @SerializedName("Property")
  @Expose
  private String property;

  @SerializedName("Attributes")
  @Expose
  private Attributes attributes;

  public void setSubscriptionType(String subscriptionType) {
    this.subscriptionType = subscriptionType;
  }

  public String getSubscriptionType() {
    return subscriptionType;
  }

  public void setAcceptContentType(String acceptContentType) {
    this.acceptContentType = acceptContentType;
  }

  public String getAcceptContentType() {
    return acceptContentType;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public Attributes getAttributes() {
    return attributes;
  }

  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
  }
}