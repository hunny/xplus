package com.xplus.commons.thread.senior.threadcommicate.waitnotifycase1;

import java.util.ArrayList;
import java.util.List;

public class MyList {

  private static List<String> list = new ArrayList<String>();

  public static void add() {
    list.add("sth");
  }

  public static int getSize() {
    return list.size();
  }
}