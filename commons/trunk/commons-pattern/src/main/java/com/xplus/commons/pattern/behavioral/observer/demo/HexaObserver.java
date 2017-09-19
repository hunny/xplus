package com.xplus.commons.pattern.behavioral.observer.demo;

public class HexaObserver implements Observer {

	private Subject subject;

	public HexaObserver(Subject subject) {
		this.subject = subject;
		this.subject.attach(this);
	}

	@Override
	public void update() {
		System.out.println("Octal String: " + Integer.toOctalString(subject.getState()));
	}

}
