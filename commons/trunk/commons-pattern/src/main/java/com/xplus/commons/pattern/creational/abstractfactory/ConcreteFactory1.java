package com.xplus.commons.pattern.creational.abstractfactory;

public class ConcreteFactory1 implements Factory {

	@Override
	public ProductA createProductA() {
		return new ProductA1();
	}

	@Override
	public ProductB createProductB() {
		return new ProductB1();
	}

}
