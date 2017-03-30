package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessKey extends Hateoas {

  @SerializedName("Secret")
  @Expose
  private Object secret;

  @SerializedName("Key")
  @Expose
  private String key;

  @SerializedName("Name")
  @Expose
  private String name;

  public void setSecret(Object secret) {
    this.secret = secret;
  }

  public Object getSecret() {
    return secret;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}