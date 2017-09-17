package com.xplus.commons.pattern.behavioral.mediator;

public class Client {

	public static void main(String[] args) {

		Mediator mediator = new ConcreteMediator();
		
		Colleague colleague = new ConcreteColleague(mediator);
		
		mediator.register(colleague);
		mediator.register(colleague);
		
		colleague.method2();
	}

}
