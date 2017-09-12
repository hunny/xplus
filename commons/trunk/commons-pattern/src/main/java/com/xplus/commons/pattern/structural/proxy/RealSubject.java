package com.xplus.commons.pattern.structural.proxy;

public class RealSubject implements Subject {

	@Override
	public void request() {
		System.out.println("真实的业务请求对象。");
	}

}
