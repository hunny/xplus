package com.xplus.commons.pattern.behavioral.observer;

public class Client {

	public static void main(String[] args) {
		Subject subject = new ConcreteSubject();
		subject.attach(new ConcreteObserverA());
		subject.attach(new ConcreteObserverB());
		subject.notifyObjects();
	}

}
