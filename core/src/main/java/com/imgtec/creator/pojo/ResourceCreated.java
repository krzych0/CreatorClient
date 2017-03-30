package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ResourceCreated extends Hateoas {

  @SerializedName("ID")
  @Expose
  private String id;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

}