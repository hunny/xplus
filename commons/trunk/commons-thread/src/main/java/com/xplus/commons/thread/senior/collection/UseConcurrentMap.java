package com.xplus.commons.thread.senior.collection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UseConcurrentMap {
  
  public static void main(String[] args) {
    
    ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
    map.put("k1", "v1");
    map.put("k2", "v2");
    map.put("k3", "v3");
    map.put("k4", "v4");
    map.putIfAbsent("k4", "v4");
    
    for (Map.Entry<String, Object> m : map.entrySet()) {
      System.out.println("key:" + m.getKey() + ", value:" + m.getValue());
    }
  }

}
