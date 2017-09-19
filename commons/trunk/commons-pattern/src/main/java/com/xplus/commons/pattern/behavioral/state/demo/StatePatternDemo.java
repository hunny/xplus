package com.xplus.commons.pattern.behavioral.state.demo;

public class StatePatternDemo {
	public static void main(String[] args) {
		Context context = new Context();

		StartState startState = new StartState();
		startState.action(context);

		System.out.println(context.getState().toString());

		StopState stopState = new StopState();
		stopState.action(context);

		System.out.println(context.getState().toString());
	}
}
