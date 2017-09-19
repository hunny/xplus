package com.xplus.commons.pattern.behavioral.state;

public class ConcreteStateB implements State {

	@Override
	public void handle() {
		System.out.println("我是状态B。");
	}

}
