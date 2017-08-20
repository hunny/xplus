/**
 * 
 */
package com.xplus.commons.pattern.creational.factorymethod;

/**
 * @author hunnyhu
 *
 */
public class ConcreteFactoryA implements Factory {

	@Override
	public Product factoryMethod() {
		System.out.println("工厂方法模式中的具体工厂A。");
		return new ConcreteProductA();
	}

}
