package com.xplus.commons.pattern.structural.flyweight;

public class UnsharedConcreteFlyweight implements Flyweight {

	@Override
	public void operation(String extrinsicState) {
		System.out.println("非共享的享元实现类：" + extrinsicState);
	}

}
