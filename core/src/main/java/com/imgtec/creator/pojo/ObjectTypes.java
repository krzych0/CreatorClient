package com.imgtec.creator.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ObjectTypes extends Hateoas {

  @SerializedName("PageInfo")
  @Expose
  private PageInfo pageInfo;

  @SerializedName("Items")
  @Expose
  private List<ObjectType> items = new ArrayList<>();

  public PageInfo getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }

  public List<ObjectType> getItems() {
    return items;
  }

  public void setItems(List<ObjectType> items) {
    this.items = items;
  }

}
