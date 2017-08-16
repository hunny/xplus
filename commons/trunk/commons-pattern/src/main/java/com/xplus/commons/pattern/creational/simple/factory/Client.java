package com.xplus.commons.pattern.creational.simple.factory;

public class Client {

	public static void main(String[] args) {
		Product product = Factory.getProduct("A");
		product.methodSame();
		product.methodDiff();
		
		System.out.println("=========");
		
		product = Factory.getProduct("B");
		product.methodSame();
		product.methodDiff();
	}
	
}
