package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Notifications<T extends Hateoas> extends Hateoas {
  @SerializedName("Items")
  @Expose
  private List<Notification<T>> items = null;

  public List<Notification<T>> getItems() {
    return items;
  }

  public void setItems(List<Notification<T>> items) {
    this.items = items;
  }
}
