package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Components extends Hateoas {

  @SerializedName("Version")
  @Expose
  private String version;

  @SerializedName("Name")
  @Expose
  private String name;

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}