package com.imgtec.creator.subscription;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.imgtec.creator.pojo.Hateoas;

public class DummyValue extends Hateoas{

  @SerializedName("InstanceID")
  @Expose
  private String instanceID;
  @SerializedName("SensorValue")
  @Expose
  private Integer sensorValue;

  public String getInstanceID() {
    return instanceID;
  }

  public void setInstanceID(String instanceID) {
    this.instanceID = instanceID;
  }

  public Integer getSensorValue() {
    return sensorValue;
  }

  public void setSensorValue(Integer sensorValue) {
    this.sensorValue = sensorValue;
  }


}
