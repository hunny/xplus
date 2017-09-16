package com.xplus.commons.pattern.behavioral.interpreter;

/**
 * 声明了抽象的解释操作，它是所有终结符表达式和非终结符表达式的声明。
 *
 */
public interface Expression {

	void interpret(Context context);
	
}
