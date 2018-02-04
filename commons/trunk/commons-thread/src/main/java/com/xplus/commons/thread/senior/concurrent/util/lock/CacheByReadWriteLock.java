package com.xplus.commons.thread.senior.concurrent.util.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheByReadWriteLock {
  private Map<String, Object> map = new HashMap<String, Object>();
  private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
  private Lock rLock = rwl.readLock();
  private Lock wLock = rwl.writeLock();

  // 获取一个key对应的value
  public final Object get(String key) {
    rLock.lock();
    try {
      Thread.sleep(new Random().nextInt(1000));
      Object obj = map.get(key);
      System.out.println(Thread.currentThread().getName() + "获取" + key + "完毕。");
      return obj;
    } catch (InterruptedException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      rLock.unlock();
    }
  }

  // 设置key对应的value并返回旧的value
  public final Object put(String key, Object value) {
    wLock.lock();
    try {
      Thread.sleep(new Random().nextInt(1000));
      Object obj = map.put(key, value);
      System.out.println(Thread.currentThread().getName() + "放入" + key + "完毕。");
      return obj;
    } catch (InterruptedException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      wLock.unlock();
    }
  }

  // 清空缓存
  public final void clear() {
    wLock.lock();
    try {
      map.clear();
    } finally {
      wLock.unlock();
    }
  }

  public static void main(String[] args) {
    final CacheByReadWriteLock cache = new CacheByReadWriteLock();
    for (int i = 0; i < 20; i++) {
      final int NO = i;
      new Thread(new Runnable() {
        @Override
        public void run() {
          cache.put("key-" + NO, "value-" + NO);
        }
      }).start();
    }
    for (int i = 0; i < 20; i++) {
      final int NO = i;
      new Thread(new Runnable() {
        @Override
        public void run() {
          System.out.println(cache.get("key-" + NO));;
        }
      }).start();
    }
    for (int i = 0; i < 20; i++) {
      final int NO = i;
      new Thread(new Runnable() {
        @Override
        public void run() {
          System.out.println(cache.get("key-" + NO));;
        }
      }).start();
    }
  }

}
