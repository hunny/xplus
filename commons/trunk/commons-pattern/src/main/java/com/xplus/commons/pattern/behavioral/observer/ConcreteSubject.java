package com.xplus.commons.pattern.behavioral.observer;

public class ConcreteSubject extends Subject {

	// 实现通知方法
	@Override
	public void notifyObjects() {
		// 遍历观察者集合，调用每一个观察者的响应方法
		for (Observer obs : observers) {
			obs.update();
		}
	}

}
