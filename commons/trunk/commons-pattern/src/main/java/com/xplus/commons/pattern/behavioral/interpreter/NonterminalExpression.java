package com.xplus.commons.pattern.behavioral.interpreter;

public class NonterminalExpression implements Expression {

	public NonterminalExpression(Expression...expressions) {
	}

	@Override
	public void interpret(Context context) {
		// 递归调用每一个组成部分的interpret()方法
		// 在递归调用时指定组成部分的连接方式，即非终结符的功能
	}

}
