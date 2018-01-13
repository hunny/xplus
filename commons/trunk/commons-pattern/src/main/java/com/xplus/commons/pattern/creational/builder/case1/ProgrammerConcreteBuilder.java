package com.xplus.commons.pattern.creational.builder.case1;

/**
 * ConcreteBuilder（具体建造者）
 */
public class ProgrammerConcreteBuilder implements ProgrammerBuilder {

  private ProgrammerProduct programmer = new ProgrammerProduct();
  
  public static ProgrammerBuilder newBuilder() {
    return new ProgrammerConcreteBuilder();
  }
  
  @Override
  public ProgrammerBuilder buildCPlus(boolean cplus) {
    programmer.setCplus(cplus);
    return this;
  }

  @Override
  public ProgrammerBuilder buildPython(boolean python) {
    programmer.setPython(python);
    return this;
  }

  @Override
  public ProgrammerBuilder buildJava(boolean java) {
    programmer.setJava(java);
    return this;
  }

  @Override
  public ProgrammerBuilder buildC(boolean c) {
    programmer.setC(c);
    return this;
  }

  @Override
  public ProgrammerBuilder buildKotlin(boolean kotlin) {
    programmer.setKotlin(kotlin);
    return this;
  }

  @Override
  public ProgrammerProduct build() {
    return programmer;
  }

}
