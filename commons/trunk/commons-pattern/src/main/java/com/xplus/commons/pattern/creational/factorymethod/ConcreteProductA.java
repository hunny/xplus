/**
 * 
 */
package com.xplus.commons.pattern.creational.factorymethod;

/**
 * @author hunnyhu
 *
 */
public class ConcreteProductA implements Product {

	@Override
	public void productMethod() {
		System.out.println("工厂方法模式中的产品A。");
	}

}
