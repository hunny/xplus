package com.xplus.commons.guava.basic;

import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Preconditions;

/**
 * 前置条件：让方法调用的前置条件判断更简单。
 * 
 * Guava在Preconditions类中提供了若干前置条件判断的实用方法。每个方法都有三个变种：
 * 
 * 没有额外参数：抛出的异常中没有错误消息； 有一个Object对象作为额外参数：抛出的异常使用Object.toString() 作为错误消息；
 * 有一个String对象作为额外参数，并且有一组任意数量的附加Object对象：这个变种处理异常消息的方式有点类似printf，但考虑GWT的兼容性和效率，只支持%s指示符。例如：
 * <code>
 * checkArgument(i >= 0, "Argument was %s but expected nonnegative", i); 
 * checkArgument(i < j, "Expected i < j, but %s > %s", i, j);
 * </code>
 * 参考地址{@link http://ifeve.com/google-guava-preconditions/}。
 * @author huzexiong
 *
 */
public class A_1_2_PreconditionTest {

  @Test(expected = NullPointerException.class)
  public void test() {
    Integer five = null;
    five = Objects.requireNonNull(five);
  }
  
  @Test
  public void test1() {
    Integer five = 5;
    Assert.assertEquals("相同", true, 5 == Objects.requireNonNull(five));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void test3() {
    Preconditions.checkArgument(false, "OK %s", " HAHA ");
  }
  
  @Test(expected = NullPointerException.class)
  public void test4() {
    Preconditions.checkNotNull(null);
  }
  
}
