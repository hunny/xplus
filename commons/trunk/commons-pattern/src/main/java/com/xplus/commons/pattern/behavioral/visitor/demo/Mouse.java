package com.xplus.commons.pattern.behavioral.visitor.demo;

public class Mouse  implements ComputerPart {

  @Override
  public void accept(ComputerPartVisitor computerPartVisitor) {
     computerPartVisitor.visit(this);
  }
}
