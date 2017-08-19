package com.xplus.commons.pattern.creational.simplefactory;

public class Factory {
	
	//静态工厂方法
	public static Product getProduct(String type) {
		Product product = null;
		if ("A".equalsIgnoreCase(type)) {
			product = new ConcreteProductA();
		} else if ("B".equalsIgnoreCase(type)) {
			product = new ConcreteProductB();
		}
		return product;
	}

}
