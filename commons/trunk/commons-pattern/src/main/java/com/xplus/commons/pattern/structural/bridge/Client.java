package com.xplus.commons.pattern.structural.bridge;

public class Client {

  public static void main(String[] args) {

    Abstraction abstraction = new RefinedAbstraction();

    abstraction.setImpl(new ConcretelImplementorA());

    abstraction.operation();

    abstraction.setImpl(new ConcretelImplementorB());

    abstraction.operation();

  }

}
