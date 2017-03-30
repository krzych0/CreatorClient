package com.imgtec.creator.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Instances<T extends Hateoas> extends Hateoas {

  @SerializedName("PageInfo")
  @Expose
  private PageInfo pageInfo;

  @SerializedName("Items")
  @Expose
  private List<T> items = new ArrayList<>();

  public PageInfo getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }

  public List<T> getItems() {
    return items;
  }

  public void setItems(List<T> items) {
    this.items = items;
  }
}
