package com.xplus.commons.pattern.behavioral.memento;

/**
 * 原发器类Originator，在真实业务中，原发器类是一个具体的业务类，它包含一些用于存储成员数据的属性，典型代码
 */
public class Originator {

	private String state;

	public Originator() {
	}

	// 创建一个备忘录对象
	public Memento createMemento() {
		return new Memento(this);
	}

	// 根据备忘录对象恢复原发器状态
	public void restoreMemento(Memento m) {
		state = m.getState();
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}
}
