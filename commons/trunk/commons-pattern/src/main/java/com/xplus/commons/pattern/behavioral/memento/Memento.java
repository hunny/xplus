package com.xplus.commons.pattern.behavioral.memento;

/**
 * 备忘录类，默认可见性，包内可见
 */
class Memento {
	private String state;

	public Memento(Originator o) {
		state = o.getState();
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}
}
