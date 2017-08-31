package com.xplus.commons.pattern.node;

// 简单句子解释：非终结符表达式
public class SentenceNode implements Node {

	private Node direction;
	private Node action;
	private Node distence;
	
	public SentenceNode(Node direction, Node action, Node distence) {
		this.direction = direction;
		this.action = action;
		this.distence = distence;
	}

	// 简单句子的解释操作
	@Override
	public String interpret() {
		return direction.interpret() + action.interpret() + distence.interpret();
	}

}
