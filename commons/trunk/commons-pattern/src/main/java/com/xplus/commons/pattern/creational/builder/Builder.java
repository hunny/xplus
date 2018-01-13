package com.xplus.commons.pattern.creational.builder;

/**
 * Builder（抽象建造者）
 * <p>
 * 生成器接口，定义创建一个产品对象所需的各个部件的操作
 * <p>
 * 它为创建一个产品对象的各个部件指定抽象接口
 */
public interface Builder {

  /** 
   * 示意方法，构建某个部件 
   */  
  public void buildPart1();
  /** 
   * 示意方法，构建某个部件 
   */  
  public void buildPart2();
  
  public Product getProcuct();
  
}
