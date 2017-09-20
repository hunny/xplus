package com.xplus.commons.pattern.behavioral.template;

public class Client {

	public static void main(String[] args) {
		AbstractTemplate template = new ConcreteTemplateA();
		template.template();
		template = new ConcreteTemplateB();
		template.template();
	}

}
