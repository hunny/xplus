package com.xplus.commons.pattern.structural.decorator;

public class Decorator implements Component {

  private Component component; // 维持一个对抽象构件对象的引用

  public Decorator(Component component) {// 注入一个抽象构件类型的对象
    this.component = component;
  }

  public void operation() {
    System.out.println("装饰模式调用原有业务方法");
    component.operation(); // 调用原有业务方法
  }

}
