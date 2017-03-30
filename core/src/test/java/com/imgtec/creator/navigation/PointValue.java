package com.imgtec.creator.navigation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.imgtec.creator.pojo.Hateoas;


public class PointValue extends Hateoas {

  @SerializedName("SetPointValue")
  @Expose
  private float value;

  @SerializedName("InstanceID")
  @Expose
  private String instanceID;


  public void setPointValue(float pointValue) {
    this.value = pointValue;
  }

  public float getPointValue() {
    return value;
  }

  public void setInstanceID(String instanceID) {
    this.instanceID = instanceID;
  }

  public String getInstanceID() {
    return instanceID;
  }

}