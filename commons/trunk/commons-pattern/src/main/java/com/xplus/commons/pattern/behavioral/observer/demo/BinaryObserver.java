package com.xplus.commons.pattern.behavioral.observer.demo;

public class BinaryObserver implements Observer {

	private Subject subject;

	public BinaryObserver(Subject subject) {
		this.subject = subject;
		this.subject.attach(this);
	}

	@Override
	public void update() {
		System.out.println("Hex String: " + Integer.toHexString(subject.getState()).toUpperCase());
	}

}
