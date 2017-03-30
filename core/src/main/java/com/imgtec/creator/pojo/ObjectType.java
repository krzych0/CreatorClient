package com.imgtec.creator.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjectType extends Hateoas {

  @SerializedName("ObjectTypeID")
  @Expose
  private String objectTypeID;

  public String getObjectTypeID() {
    return objectTypeID;
  }

  public void setObjectTypeID(String objectTypeID) {
    this.objectTypeID = objectTypeID;
  }
}
