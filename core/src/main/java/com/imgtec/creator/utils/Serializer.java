package com.imgtec.creator.utils;

import com.imgtec.creator.pojo.Hateoas;

/**
 * Interface to serialize POJO to json representation.
 */
public interface Serializer {

  <T extends Hateoas> String toJSON(T object);
}
