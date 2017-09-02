package com.xplus.commons.pattern.structural.adapter;

public class Client {
  
  public static void main(String[] args) {

    Adaptee adaptee = new Adaptee();
    
    Target target = new Adapter(adaptee);
    
    target.request();
    
  }
  
}
