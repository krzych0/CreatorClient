package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties extends Hateoas {

  @SerializedName("SerialisationName")
  @Expose
  private String serialisationName;

  @SerializedName("IsCollection")
  @Expose
  private boolean isCollection;

  @SerializedName("IsMandatory")
  @Expose
  private boolean isMandatory;

  @SerializedName("Access")
  @Expose
  private String access;

  @SerializedName("DataType")
  @Expose
  private String dataType;

  @SerializedName("PropertyID")
  @Expose
  private String propertyID;

  @SerializedName("Name")
  @Expose
  private String name;

  public void setSerialisationName(String serialisationName) {
    this.serialisationName = serialisationName;
  }

  public String getSerialisationName() {
    return serialisationName;
  }

  public void setIsCollection(boolean isCollection) {
    this.isCollection = isCollection;
  }

  public boolean isIsCollection() {
    return isCollection;
  }

  public void setIsMandatory(boolean isMandatory) {
    this.isMandatory = isMandatory;
  }

  public boolean isIsMandatory() {
    return isMandatory;
  }

  public void setAccess(String access) {
    this.access = access;
  }

  public String getAccess() {
    return access;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getDataType() {
    return dataType;
  }

  public void setPropertyID(String propertyID) {
    this.propertyID = propertyID;
  }

  public String getPropertyID() {
    return propertyID;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}