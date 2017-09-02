package com.xplus.commons.pattern.structural.adapter;

/**
 * 对象适配器模式。
 * 
 * <p>
 * 对象适配器模式中适配器和适配者之间是关联关系。
 * 
 * @author huzexiong
 */
public class AdapterObject implements Target {

  private Adaptee adaptee;
  
  public AdapterObject(Adaptee adaptee) {
    this.adaptee = adaptee;
  }
  
  @Override
  public void request() {
    adaptee.specificRequest();
  }

}
