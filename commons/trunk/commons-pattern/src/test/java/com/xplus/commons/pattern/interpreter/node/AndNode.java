package com.xplus.commons.pattern.interpreter.node;

/**
 * @author hunnyhu
 */
public class AndNode implements Node {

	private Node left; // And左表达式
	private Node right;// And右表达式
	
	public AndNode(Node left, Node right) {
		this.left = left;
		this.right = right;
	}
	
	// And表达式操作
	@Override
	public String interpret() {
		return this.left.interpret() + "再" + this.right.interpret();
	}

}
