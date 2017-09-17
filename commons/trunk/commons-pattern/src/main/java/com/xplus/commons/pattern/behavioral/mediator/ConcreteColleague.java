package com.xplus.commons.pattern.behavioral.mediator;

public class ConcreteColleague extends Colleague {

	public ConcreteColleague(Mediator mediator) {
		super(mediator);
	}

	@Override
	public void method1() {
		System.out.println("同事对象自己的执行方法。");
	}

}
