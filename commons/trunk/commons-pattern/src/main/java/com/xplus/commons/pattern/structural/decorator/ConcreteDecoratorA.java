package com.xplus.commons.pattern.structural.decorator;

/**
 * 在抽象装饰类Decorator中定义了一个Component类型的对象component，
 * 维持一个对抽象构件对象的引用，并可以通过构造方法或Setter方法将一个Component类型的对象注入进来，
 * 同时由于Decorator类实现了抽象构件Component接口， 因此需要实现在其中声明的业务方法operation()，
 * 需要注意的是在Decorator中并未真正实现operation()方法， 而只是调用原有component对象的operation()方法，
 * 它没有真正实施装饰，而是提供一个统一的接口，将具体装饰过程交给子类完成。
 * 
 * @author huzexiong
 *
 */
public class ConcreteDecoratorA extends Decorator {

  public ConcreteDecoratorA(Component component) {
    super(component);
  }

  public void operation() {
    super.operation(); // 调用原有业务方法
    addedBehavior(); // 调用新增业务方法
  }

  // 新增业务方法
  public void addedBehavior() {
    System.out.println("具体装饰模式A中新增业务方法。");
  }
}
