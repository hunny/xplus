package com.xplus.commons.pattern.structural.composite;

public class Leaf extends Component {

  @Override
  public void add(Component c) {
    // 异常处理或错误提示
    throw new UnsupportedOperationException();
  }

  @Override
  public void remove(Component c) {
    // 异常处理或错误提示
    throw new UnsupportedOperationException();
  }

  @Override
  public Component getChild(int i) {
    // 异常处理或错误提示
    throw new UnsupportedOperationException();
  }

  @Override
  public void operation() {
    System.out.println("叶子构件具体业务方法的实现");
  }

}
