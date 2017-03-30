package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ObjectDefinitions extends Hateoas {

  @SerializedName("PageInfo")
  @Expose
  private PageInfo pageInfo;

  @SerializedName("Items")
  @Expose
  private List<ObjectDefinition> items;

  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }

  public PageInfo getPageInfo() {
    return pageInfo;
  }

  public void setItems(List<ObjectDefinition> items) {
    this.items = items;
  }

  public List<ObjectDefinition> getItems() {
    return items;
  }

}