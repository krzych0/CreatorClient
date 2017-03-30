package com.imgtec.creator.pojo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscriptions extends Hateoas {

  @SerializedName("PageInfo")
  @Expose
  private PageInfo pageInfo;

  @SerializedName("Items")
  @Expose
  private List<Subscription> items;

  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }

  public PageInfo getPageInfo() {
    return pageInfo;
  }

  public void setItems(List<Subscription> items) {
    this.items = items;
  }

  public List<Subscription> getItems() {
    return items;
  }
}