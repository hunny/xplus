package com.xplus.commons.pattern.creational.builder.case1;

/**
 * Director（指挥者）
 */
public class ProgrammerDirector {

  public ProgrammerProduct makeJava() {
    return ProgrammerConcreteBuilder //
        .newBuilder() //
        .buildJava(true) //
        .build();//
  }

  public ProgrammerProduct makeC() {
    return ProgrammerConcreteBuilder //
        .newBuilder() //
        .buildC(true) //
        .build();//
  }

  public ProgrammerProduct makePython() {
    return ProgrammerConcreteBuilder //
        .newBuilder() //
        .buildPython(true) //
        .build();//
  }

  public ProgrammerProduct makeKotlin() {
    return ProgrammerConcreteBuilder //
        .newBuilder() //
        .buildKotlin(true) //
        .build();//
  }

  public ProgrammerProduct makeCPlus() {
    return ProgrammerConcreteBuilder //
        .newBuilder() //
        .buildCPlus(true) //
        .build();//
  }

}
