package com.imgtec.creator.navigation;

import com.google.gson.JsonObject;

/**
 * Navigator used to simplify traversing across links.
 */
public interface Navigator extends Navigatable {

  JsonObject getNode();

}
