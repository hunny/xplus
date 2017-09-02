package com.xplus.commons.pattern.structural.adapter;

public class Client {
  
  public static void main(String[] args) {

    Adaptee adaptee = new Adaptee();
    
    Target target = new AdapterObject(adaptee);// 对象适配模式
    
    target.request();
    
    target = new AdapterClass();// 类适配模式
    
    target.request();
    
  }
  
}
