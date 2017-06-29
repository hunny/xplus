package com.xplus.commons.guava.basic;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Optional;

/**
 * 使用和避免null：null是模棱两可的，会引起令人困惑的错误，有些时候它让人很不舒服。<br>
 * 很多Guava工具类用快速失败拒绝null值，而不是盲目地接受。
 * 
 * @author huzexiong
 *
 */
public class A_1_1_OptionalTest {

  @Test
  public void test() {
    Optional<Integer> possible = Optional.of(5);
    Assert.assertEquals("相同", true, possible.isPresent());
    Assert.assertEquals("相同", true, 5 == possible.get());
    Assert.assertEquals("相同", true, 5 == possible.or(3));
    Assert.assertEquals("相同", true, possible.asSet().size() == 1);
    Optional<Integer[]> possibles = Optional.of(new Integer[] {
        1, 2, 3
    });
    Assert.assertEquals("相同", true, possibles.asSet().size() == 1);
  }
  
  @Test
  public void test2() {
    Optional<Integer> possible = Optional.absent();
    Assert.assertEquals("相同", false, possible.isPresent());
    Assert.assertEquals("相同", true, null == possible.orNull());
    Assert.assertEquals("相同", true, 3 == possible.or(3));
    Assert.assertEquals("相同", true, Optional.absent().equals(Optional.fromNullable(null)));
  }
  
  @Test
  public void test3() {
    Optional<Integer> possible = Optional.of(8);
    Assert.assertEquals("相同", true, possible.isPresent());
    Assert.assertEquals("相同", false, null == possible.orNull());
    Assert.assertEquals("相同", true, 8 == possible.or(3));
    Assert.assertEquals("相同", true, Optional.absent().equals(Optional.fromNullable(null)));
  }
  
}
