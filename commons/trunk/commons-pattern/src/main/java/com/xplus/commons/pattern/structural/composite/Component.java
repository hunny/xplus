package com.xplus.commons.pattern.structural.composite;

public abstract class Component {

  public abstract void add(Component c); //增加成员  
  
  public abstract void remove(Component c); //删除成员  
  
  public abstract Component getChild(int i); //获取成员  
  
  public abstract void operation();  //业务方法  
  
}
