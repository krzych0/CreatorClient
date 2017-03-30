package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Bootstrap extends Hateoas {

  @SerializedName("Url")
  @Expose
  private String url;

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}