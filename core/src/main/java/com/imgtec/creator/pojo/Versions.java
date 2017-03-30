package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Versions extends Hateoas {

  @SerializedName("Components")
  @Expose
  private List<Components> components;

  @SerializedName("BuildNumber")
  @Expose
  private String buildNumber;

  public void setComponents(List<Components> components) {
    this.components = components;
  }

  public List<Components> getComponents() {
    return components;
  }

  public void setBuildNumber(String buildNumber) {
    this.buildNumber = buildNumber;
  }

  public String getBuildNumber() {
    return buildNumber;
  }
}