package com.xplus.commons.pattern.structural.decorator;

public class Client {

  public static void main(String[] args) {

    Component component = new ConcreteComponent();

    Decorator decorator = new ConcreteDecoratorA(component);

    decorator.operation();

    decorator = new ConcreteDecoratorB(component);

    decorator.operation();
  }

}
