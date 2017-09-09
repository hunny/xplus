package com.xplus.commons.pattern.structural.flyweight;

public class CompositeConcreteFlyweight implements Flyweight {

	@Override
	public void operation(String extrinsicState) {

	}
	
	public boolean add(Flyweight flyweight) {
		return true;
	}
	
	public boolean remove(Flyweight flyweight) {
		return true;
	}

}
