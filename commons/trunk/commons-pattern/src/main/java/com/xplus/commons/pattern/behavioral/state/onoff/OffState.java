package com.xplus.commons.pattern.behavioral.state.onoff;

public class OffState implements State {
	
	public void on(Switch s) {
		System.out.println("打开！");
		s.setState(s.getState("on"));
	}

	public void off(Switch s) {
		System.out.println("已经关闭！");
	}
	
}
