package com.xplus.commons.pattern.interpreter.commandnode;

public class Client {

	public static void main(String[] args) {
		String text = "LOOP 3 PRINT 杨过 SPACE SPACE PRINT 小龙女 BREAK END PRINT 郭靖 SPACE SPACE PRINT 黄蓉 BREAK PRINT OK BREAK LOOP 5 PRINT 收到。 SPACE";
		Context context = new Context(text);
		Node node = new ExpressionNode();
		node.interpret(context);
		node.execute();
	}

}
