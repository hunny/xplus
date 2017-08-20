/**
 * 
 */
package com.xplus.commons.pattern.creational.factorymethod;

/**
 * @author hunnyhu
 *
 */
public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Factory factory = null;
		Product product = null;
		factory = new ConcreteFactoryA();
		product = factory.factoryMethod();
		product.productMethod();
		
		factory = new ConcreteFactoryB();
		product = factory.factoryMethod();
		product.productMethod();
	}

}
