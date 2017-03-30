package com.imgtec.creator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import okhttp3.HttpUrl;

public class MockResponseProvider {

  static final Logger logger = LoggerFactory.getLogger(MockResponseProvider.class.getSimpleName());
  public static final MockResponseProvider DATA_PROVIDER = new MockResponseProvider();

  public static String getFile(String fileName) {
    StringBuilder result = new StringBuilder("");
    //Get file from resources folder
    ClassLoader classLoader = MockResponseProvider.class.getClassLoader();
    File file = new File(classLoader.getResource(fileName).getFile().replace("%20", " "));
    try {
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        result.append(line).append('\n');
      }
      scanner.close();
    } catch (IOException e) {
      logger.warn("Get file failed!", e);
    }
    return result.toString();
  }

  public String getClients(HttpUrl url) {
    return getFile("clients_mock_response.json")
        .replaceAll("%s", url.toString()
            .replaceAll("/$", ""));
  }

  public String getClient(HttpUrl url) {
    return getFile("client_mock_response.json")
        .replaceAll("%s", url.toString()
            .replaceAll("/$", ""));
  }

  public String getObjecttypes(HttpUrl url) {
    return getFile("objecttypes_mock_response.json")
            .replaceAll("%s", url.toString()
                .replaceAll("/$", ""));
  }

  public String getInstances(HttpUrl url) {
    return getFile("instances_mock_response.json")
            .replaceAll("%s", url.toString()
            .replaceAll("/$", ""));
  }

  public String getInstance(HttpUrl url) {
    return getFile("instance_mock_response.json")
            .replaceAll("%s", url.toString()
            .replaceAll("/$", ""));
  }
}
