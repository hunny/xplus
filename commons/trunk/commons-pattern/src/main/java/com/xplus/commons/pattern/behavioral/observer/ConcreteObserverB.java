package com.xplus.commons.pattern.behavioral.observer;

public class ConcreteObserverB implements Observer {

	// 实现响应方法
	@Override
	public void update() {
		// 具体响应代码
		System.out.println("我是具体响应订阅代码B。");
	}

}
