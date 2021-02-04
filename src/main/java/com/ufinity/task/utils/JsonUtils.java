package com.ufinity.task.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
  public static JsonNode convertToJsonNode(String jsonString) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readTree(jsonString);
  }
}
