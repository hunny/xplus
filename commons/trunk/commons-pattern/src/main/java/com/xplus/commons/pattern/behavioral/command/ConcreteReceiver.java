package com.xplus.commons.pattern.behavioral.command;

public class ConcreteReceiver implements Receiver {

	@Override
	public void action() {
		System.out.println("具体接收者执行行为。");
	}

}
