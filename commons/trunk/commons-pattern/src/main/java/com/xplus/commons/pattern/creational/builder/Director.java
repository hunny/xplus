package com.xplus.commons.pattern.creational.builder;

/**
 * 导演者，指导使用生成器的接口来构建产品对象 
 */
public class Director {

  private Builder builder = null;
  
  /**
   * 构造方法，传人生成器对象
   */
  public Director(Builder builder) {
    this.builder = builder;
  }
  
  /**
   * 示意方法，指导生成器构建最终的产品对象
   */
  public void construct() {
    builder.buildPart1();
    builder.buildPart2();
  }
  
  public Product getProduct() {
    return builder.getProcuct();
  }
  
}
