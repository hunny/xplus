package com.xplus.commons.pattern.behavioral.state;

public class ConcreteStateA implements State {

	@Override
	public void handle() {
		System.out.println("我是状态A。");
	}

}
