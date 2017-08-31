package com.xplus.commons.pattern.interpreter.commandnode;

//抽象节点类：抽象表达式 
public interface Node {

	/**
	 * 声明一个方法用于解释语句
	 * 
	 * @param text
	 */
	void interpret(Context text);

	/**
	 * 声明一个方法用于执行标记对应的命令
	 */
	void execute();

}
