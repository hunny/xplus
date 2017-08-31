package com.xplus.commons.pattern.interpreter.commandnode;

//循环命令节点类：非终结符表达式
public class LoopCommandNode implements Node {
	
	private int number; // 循环次数
	private Node node; // 循环语句中的表达式

	// 解释循环命令
	public void interpret(Context context) {
		context.skipToken("LOOP");
		number = context.currentNumber();
		context.nextToken();
		node = new ExpressionNode(); // 循环语句中的表达式
		node.interpret(context);
	}

	public void execute() {
		for (int i = 0; i < number; i++)
			node.execute();
	}
}
