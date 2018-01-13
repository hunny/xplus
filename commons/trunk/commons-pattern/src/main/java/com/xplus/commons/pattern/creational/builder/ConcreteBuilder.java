package com.xplus.commons.pattern.creational.builder;

/**
 * ConcreteBuilder（具体建造者）
 * <p>
 * 具体的生成器实现对象
 */
public class ConcreteBuilder implements Builder {

  private Product product = null;

  @Override
  public void buildPart1() {
    // 构建某个部件的功能处理
    // product
  }
  
  @Override
  public void buildPart2() {
    // 构建某个部件的功能处理
    // product
  }

  @Override
  public Product getProcuct() {
    return product;
  }

}
