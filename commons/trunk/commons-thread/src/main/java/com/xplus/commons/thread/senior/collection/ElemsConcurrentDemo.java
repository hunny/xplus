package com.xplus.commons.thread.senior.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ElemsConcurrentDemo {

  public static void main(String[] args) {

    final Vector<String> elems = new Vector<String>();
    final List<String> list = new ArrayList<String>();

    for (int i = 0; i < 100; i++) {
      elems.add("元素" + i);
      list.add("元素" + i);
    }
    
    concurrentModifyException(elems);
    removeConcurrentColletion(elems);
    
    List<String> conelems = Collections.synchronizedList(list);
    concurrentModifyException(conelems);
    removeConcurrentColletion(conelems);

  }

  protected static void removeConcurrentColletion(final List<String> elems) {
    for (int i = 0; i < 10; i++) {
      new Thread("线程" + i) {
        @Override
        public void run() {
          while (true) {
            if (elems.isEmpty())
              break;
            System.out.println("List线程名称" //
                + Thread.currentThread().getName() //
                + "值：" //
                + elems.remove(0));
          }
        }
      }.start();
    }
  }
  protected static void removeConcurrentColletion(final Vector<String> elems) {
    for (int i = 0; i < 10; i++) {
      new Thread("线程" + i) {
        @Override
        public void run() {
          while (true) {
            if (elems.isEmpty())
              break;
            System.out.println("Vector线程名称" //
                + Thread.currentThread().getName() //
                + "值：" //
                + elems.remove(0));
          }
        }
      }.start();
    }
  }

  protected static void concurrentModifyException(final Vector<String> elems) {
    try {
      for (Iterator<String> iterator = elems.iterator(); iterator.hasNext();) {
        String str = iterator.next();
        System.out.println(str);
        elems.remove(20);
      }
    } catch (ConcurrentModificationException e) {
      System.err.println("期望中的冲突异常：");
      e.printStackTrace();
    }
  }
  protected static void concurrentModifyException(final List<String> elems) {
    try {
      for (Iterator<String> iterator = elems.iterator(); iterator.hasNext();) {
        String str = iterator.next();
        System.out.println(str);
        elems.remove(20);
      }
    } catch (ConcurrentModificationException e) {
      System.err.println("期望中的冲突异常：");
      e.printStackTrace();
    }
  }

}
