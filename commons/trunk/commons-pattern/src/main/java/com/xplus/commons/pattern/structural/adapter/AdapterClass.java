package com.xplus.commons.pattern.structural.adapter;

/**
 * 类适配器模式
 * 
 * <p>
 * 类适配器模式中适配器和适配者是继承关系
 * 
 * @author huzexiong
 *
 */
public class AdapterClass extends Adaptee implements Target {

  @Override
  public void request() {
    super.specificRequest();
  }

}
