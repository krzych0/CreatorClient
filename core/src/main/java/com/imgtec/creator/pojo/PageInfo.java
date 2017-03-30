package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageInfo {

  @SerializedName("TotalCount")
  @Expose
  private int totalCount;

  @SerializedName("StartIndex")
  @Expose
  private int startIndex;

  @SerializedName("ItemsCount")
  @Expose
  private int itemsCount;

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void setStartIndex(int startIndex) {
    this.startIndex = startIndex;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public void setItemsCount(int itemsCount) {
    this.itemsCount = itemsCount;
  }

  public int getItemsCount() {
    return itemsCount;
  }
}