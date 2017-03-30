package com.imgtec.creator.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for all POJO-like classes.
 */
public class Hateoas {

  @SerializedName("Links")
  @Expose
  private List<Link> links = new ArrayList<Link>();

  private Map<String, Link> linksMap = new HashMap<>();

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public Link getLinkByRel(String rel) {
    if (links.isEmpty()) {
      return null;
    }
    if (links.isEmpty() == false && linksMap.isEmpty()) {
      for (Link link : links) {
        linksMap.put(link.getRel(), link);
      }
    }
    return linksMap.get(rel);
  }
}
