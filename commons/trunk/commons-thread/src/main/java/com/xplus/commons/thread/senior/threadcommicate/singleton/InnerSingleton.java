package com.xplus.commons.thread.senior.threadcommicate.singleton;

public class InnerSingleton {

  private static class Singleton {
    private static InnerSingleton SINGLETON = new InnerSingleton();
  }
  
  public static InnerSingleton getInstance() {
    return Singleton.SINGLETON;
  }
  
}
