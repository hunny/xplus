package com.xplus.commons.pattern.behavioral.state.demo;

public class StopState implements State {

	@Override
	public void action(Context context) {
		System.out.println("Player is in stop state");
		context.setState(this);
	}

	@Override
	public String toString() {
		return "Stop State";
	}

}