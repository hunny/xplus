package com.xplus.commons.pattern.behavioral.memento;

/**
 * 负责人类Caretaker，它用于保存备忘录对象，并提供getMemento()方法用于向客户端返回一个备忘录对象，
 * 原发器通过使用这个备忘录对象可以回到某个历史状态。
 */
public class Caretaker {
	
	private Memento memento;

	public Memento getMemento() {
		return memento;
	}

	public void setMemento(Memento memento) {
		this.memento = memento;
	}
	
	
	
}
