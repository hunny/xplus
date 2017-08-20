package com.xplus.commons.pattern.creational.abstractfactory;

public class Client {

	public static void main(String[] args) {
		Factory factory = new ConcreteFactory1();
		show(factory);
		System.out.println("Change Factory ========");
		factory = new ConcreteFactory2();
		show(factory);
	}
	
	public static void show(Factory factory) {
		ProductA productA = factory.createProductA();
		ProductB productB = factory.createProductB();
		productA.productAMethod();
		productB.productBMethod();
	}

}
