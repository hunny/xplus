package com.xplus.commons.pattern.creational.builder.case1;

public class SchoolClient {

  public static void main(String[] args) {
    ProgrammerDirector director = new ProgrammerDirector();
    System.out.println("C:" + director.makeC());
    System.out.println("CPlus:" + director.makeCPlus());
    System.out.println("Java:" + director.makeJava());
    System.out.println("Kotlin:" + director.makeKotlin());
    System.out.println("Python:" + director.makePython());
  }

}
