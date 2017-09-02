package com.xplus.commons.pattern.structural.composite;

import java.util.ArrayList;
import java.util.List;

public class Composite extends Component {

  private List<Component> list = new ArrayList<Component>();

  @Override
  public void add(Component c) {
    list.add(c);
  }

  @Override
  public void remove(Component c) {
    list.remove(c);
  }

  @Override
  public Component getChild(int i) {
    return list.get(i);
  }

  @Override
  public void operation() {
    // 容器构件具体业务方法的实现
    // 递归调用成员构件的业务方法
    for (Component component : list) {
      component.operation();
    }
  }

}
