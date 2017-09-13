package com.xplus.commons.pattern.command.adder;

public class Client {

	public static void main(String[] args) {
		CalculatorContext context = new CalculatorContext();
		Command command = new ConcreteCommand();
		context.setCommand(command); // 向发送者注入命令对象

		context.compute(10);
		context.compute(5);
		context.compute(20);
		context.undo();

	}

}
