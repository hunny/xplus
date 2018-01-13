package com.xplus.commons.pattern.creational.builder;

public class Client {

  public static void main(String[] args) {
    Builder builder = new ConcreteBuilder();
    Director director = new Director(builder);
    director.construct();
    Product product = director.getProduct();
    System.out.println("Product:" + product);
  }

}
