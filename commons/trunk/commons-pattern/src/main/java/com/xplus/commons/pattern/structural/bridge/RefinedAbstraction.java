package com.xplus.commons.pattern.structural.bridge;

public class RefinedAbstraction extends Abstraction {

  @Override
  public void operation() {

    System.out.println("桥接模式方法开始");//业务代码
    
    getImpl().operationImpl();//调用实现类的方法
    
    System.out.println("桥接模式方法结束");//业务代码 
    
  }

}
