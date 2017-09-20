package com.xplus.commons.pattern.behavioral.visitor.demo;

/**
 * 定义一个表示元素的接口。
 */
public interface ComputerPart {

	public void accept(ComputerPartVisitor computerPartVisitor);
	
}
