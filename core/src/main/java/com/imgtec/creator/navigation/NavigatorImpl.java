package com.imgtec.creator.navigation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.CreatorClient;
import com.imgtec.creator.pojo.Hateoas;
import com.imgtec.creator.utils.Deserializer;
import com.imgtec.creator.utils.GsonDeserializer;
import com.imgtec.creator.utils.Verify;

import java.io.IOException;
import java.util.Map;

public class NavigatorImpl implements Navigator {

  private static final String LINKS = "Links";
  private static final String REL = "rel";
  private static final String HREF = "href";
  private static final String ITEMS = "Items";

  private final CreatorClient client;
  private String rootUrl;

  /**
   * Currently selected node.
   */
  private JsonObject node;

  public NavigatorImpl(CreatorClient creatorClient, String rootUrl) {
    this.client = creatorClient;
    this.rootUrl = Verify.checkNotNull(rootUrl, "rootUrl == null");
  }

  @Override
  public JsonObject getNode() {
    return node;
  }

  @Override
  public void reset() {
    node = null;
  }

  @Override
  public Navigatable filter(PropertyFilter filter)
      throws IOException, NavigationException {

    //JsonObject element = loadNodeElement();
    JsonObject element = getNode();
    JsonElement itemsElement = element.get(ITEMS);
    if (itemsElement != null && itemsElement.isJsonArray()) {
      JsonArray items = (JsonArray) element.get(ITEMS);
      if (items == null || items.size() == 0) {
        throw new NavigationException("No items found");
      }

      for (int i = 0; i < items.size(); ++i) {
        if (items.get(i).isJsonObject()) {
          JsonObject object = (JsonObject) items.get(i);
          for (Map.Entry<String, JsonElement> entry : object.entrySet()) {

            if (entry.getValue().isJsonArray() == false
                && filter.accept(entry.getKey(), entry.getValue().getAsString())) {
              node = object;
              break;
            }
          }
        }
      }

      if (node == element) {
        throw new NavigationException("Filter could not be applied!");
      }
    } else {
      throw new NavigationException("No Items found!");
    }

    return this;
  }

  private JsonObject loadNodeElement()
      throws IOException, NavigationException {

    final String url = node.get(HREF).getAsString();
    JsonObject element = client.get(url);
    if (element == null) {
      throw new NavigationException("Couldn't load: " + url);
    }
    //mark new node
    node = element;

    return element;
  }

  @Override
  public <T extends Hateoas> T get(Class<T> clazz, Deserializer deserializer) {
    return deserializer.fromJson(node.toString(), clazz);
  }

  @Override
  public <T extends Hateoas> T get(TypeToken<T> typeToken, Deserializer deserializer) {
    return deserializer.fromJson(node.toString(), typeToken);
  }

  @Override
  public <T extends Hateoas> T findByRel(String rel, Class<T> clazz)
      throws IOException, NavigationException {

    findByRel(rel);
    //loadNodeElement();
    return get(clazz, new GsonDeserializer());
  }

  @Override
  public Navigatable findByRel(String rel) throws IOException, NavigationException {


    JsonObject root = (getNode() == null) ? (JsonObject) client.get(rootUrl) : getNode();
    if (root == null) {
      throw new NavigationException("Who would expect that ;)");
    }

    node = root;
    if (root.get(LINKS).isJsonArray()) {
      JsonArray rootArray = root.get(LINKS).getAsJsonArray();
      for (int i = 0; i < rootArray.size(); ++i) {
        JsonElement el = rootArray.get(i);
        if (((JsonObject) el).get(REL).getAsString().equals(rel)) {
          node = (JsonObject) el;
          break;
        }
      }
    }

    if (node == root) {
      throw new NavigationException("Rel not found!");
    }

    loadNodeElement();
    return this;
  }
}
