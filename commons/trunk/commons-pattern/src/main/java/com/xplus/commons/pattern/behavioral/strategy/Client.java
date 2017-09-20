package com.xplus.commons.pattern.behavioral.strategy;

public class Client {

	public static void main(String[] args) {
		Context context = new Context();
		context.setStrategy(new ConcreteStrategyA());
		context.algorithm();
		context.setStrategy(new ConcreteStrategyB());
		context.algorithm();
	}

}
