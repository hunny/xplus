package com.xplus.commons.guava.basic;

import java.util.Comparator;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;

/**
 * 常见Object方法
 * 
 * 参见{@link http://ifeve.com/google-guava-commonobjectutilities/}。
 * 
 * @author huzexiong
 *
 */
public class A_1_3_MoreObjectTest {

  @Test
  public void test() {
    Assert.assertEquals("相同", true, com.google.common.base.Objects.equal(null, null));
    Assert.assertEquals("相同", true, java.util.Objects.equals(null, null));
    Assert.assertEquals("相同", java.util.Objects.hash("1", "2"), //
        com.google.common.base.Objects.hashCode("1", "2"));
    Assert.assertEquals("相同", "3", MoreObjects.firstNonNull(null, "3"));
    System.out
        .println(com.google.common.base.MoreObjects.toStringHelper(this).add("x", 1).toString());
    Assert.assertEquals("相同", 0, ComparisonChain.start() //
        .compare("1", "2", new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
            return Integer.parseInt(o2) - Integer.parseInt(o1) - 1;
          }
        }).result());
  }

}
