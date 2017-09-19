package com.xplus.commons.pattern.behavioral.state.demo;

public class StartState implements State {

	@Override
	public void action(Context context) {
		System.out.println("Player is in start state");
		context.setState(this);
	}

	@Override
	public String toString() {
		return "Start State";
	}
}
