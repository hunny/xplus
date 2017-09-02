package com.xplus.commons.pattern.structural.composite;

public class Client {

  public static void main(String[] args) {
    Component leaf = new Leaf();
    Component component = new Composite();
    component.add(leaf);
    component.add(leaf);
    
    Component component1 = new Composite();
    component1.add(leaf);
    component1.add(leaf);
    component1.add(leaf);
    component.add(component1);
    
    component.operation();

  }

}
