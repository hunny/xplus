package com.xplus.commons.pattern.behavioral.state.onoff;

public class OnState implements State {
	
	public void on(Switch s) {
		System.out.println("已经打开！");
	}

	public void off(Switch s) {
		System.out.println("关闭！");
		s.setState(s.getState("off"));
	}
	
}
