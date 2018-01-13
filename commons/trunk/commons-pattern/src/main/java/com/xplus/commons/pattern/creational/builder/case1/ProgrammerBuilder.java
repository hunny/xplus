package com.xplus.commons.pattern.creational.builder.case1;

/**
 * Builder（抽象建造者）
 */
public interface ProgrammerBuilder {

  ProgrammerBuilder buildCPlus(boolean cplus);
  
  ProgrammerBuilder buildPython(boolean python);
  
  ProgrammerBuilder buildJava(boolean java);
  
  ProgrammerBuilder buildC(boolean c);
  
  ProgrammerBuilder buildKotlin(boolean kotlin);
  
  ProgrammerProduct build();
}
