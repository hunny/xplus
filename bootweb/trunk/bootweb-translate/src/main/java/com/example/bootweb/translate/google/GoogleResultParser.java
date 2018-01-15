package com.example.bootweb.translate.google;

import java.util.List;

import com.example.bootweb.translate.api.Parser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleResultParser implements Parser<String, String> {

  public static GoogleResultParser newParser() {
    return new GoogleResultParser();
  }
  
  private ObjectMapper objectMapper;

  public GoogleResultParser setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String parse(String result) {
    if (null == objectMapper) {
      objectMapper = getDefaultObjectMapper();
    }
    try {
      List<Object> values = objectMapper.readValue(result, new TypeReference<List<Object>>() {
      });
      if (null != values && values.size() > 0 && values.get(0) instanceof List) {
        List<List<String>> values2 = (List<List<String>>) values.get(0);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < values2.size(); i++) {
          List<String> v = values2.get(i);
          if (null != v.get(0)) {
            buffer.append(v.get(0));
          }
        }
        return buffer.toString();
      }
      throw new IllegalArgumentException("无法解析的结果。");
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  protected ObjectMapper getDefaultObjectMapper() {
    return new ObjectMapper();
  }

}
