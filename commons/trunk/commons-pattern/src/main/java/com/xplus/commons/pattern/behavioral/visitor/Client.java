package com.xplus.commons.pattern.behavioral.visitor;

public class Client {

	public static void main(String[] args) {
		ObjectStructure objectStructure = new ObjectStructure();
		objectStructure.addElement(new ConcreteElementA());
		objectStructure.addElement(new ConcreteElementB());
		objectStructure.accept(new ConcreteVisitorA());
		objectStructure.accept(new ConcreteVisitorB());
	}

}
