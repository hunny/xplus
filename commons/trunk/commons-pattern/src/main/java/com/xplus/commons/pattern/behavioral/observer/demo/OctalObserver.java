package com.xplus.commons.pattern.behavioral.observer.demo;

public class OctalObserver implements Observer {

	private Subject subject;

	public OctalObserver(Subject subject) {
		this.subject = subject;
		this.subject.attach(this);
	}

	@Override
	public void update() {
		System.out.println("Binary String: " + Integer.toBinaryString(subject.getState()));
	}

}
