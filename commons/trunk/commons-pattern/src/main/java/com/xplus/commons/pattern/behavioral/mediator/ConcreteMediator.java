package com.xplus.commons.pattern.behavioral.mediator;

public class ConcreteMediator extends Mediator {

	@Override
	public void operation() {
		System.out.println("调用中介者模式中的方法。");
		for (Colleague colleague : colleagues) {
			colleague.method1();
		}
		System.out.println("中介者模式中的方法执行完毕。");
	}

}
