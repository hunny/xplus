package com.xplus.commons.pattern.behavioral.visitor.demo;

public class Monitor  implements ComputerPart {

  @Override
  public void accept(ComputerPartVisitor computerPartVisitor) {
     computerPartVisitor.visit(this);
  }
}
