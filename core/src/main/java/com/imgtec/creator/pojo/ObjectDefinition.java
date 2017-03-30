package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ObjectDefinition extends Hateoas {

  @SerializedName("SerialisationName")
  @Expose
  private String serialisationName;

  @SerializedName("MIMEType")
  @Expose
  private String mimeType;

  @SerializedName("ObjectID")
  @Expose
  private String objectID;

  @SerializedName("Singleton")
  @Expose
  private boolean singleton;

  @SerializedName("Properties")
  @Expose
  private List<Properties> properties;

  @SerializedName("Name")
  @Expose
  private String name;

  public void setSerialisationName(String serialisationName) {
    this.serialisationName = serialisationName;
  }

  public String getSerialisationName() {
    return serialisationName;
  }

  public void setMIMEType(String mimeType) {
    this.mimeType = mimeType;
  }

  public String getMIMEType() {
    return mimeType;
  }

  public void setObjectID(String objectID) {
    this.objectID = objectID;
  }

  public String getObjectID() {
    return objectID;
  }

  public void setSingleton(boolean singleton) {
    this.singleton = singleton;
  }

  public boolean isSingleton() {
    return singleton;
  }

  public void setProperties(List<Properties> properties) {
    this.properties = properties;
  }

  public List<Properties> getProperties() {
    return properties;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}