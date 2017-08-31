package com.xplus.commons.pattern.interpreter.commandnode;

//语句命令节点类：非终结符表达式
public class CommandNode implements Node {
	private Node node;

	public void interpret(Context context) {
		if (context.currentToken().equals("LOOP")) {// 处理LOOP循环命令
			node = new LoopCommandNode();
			node.interpret(context);
		} else {// 处理其他基本命令
			node = new PrimitiveCommandNode();
			node.interpret(context);
		}
	}

	public void execute() {
		node.execute();
	}

}
