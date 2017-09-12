package com.xplus.commons.pattern.structural.proxy;

public class Proxy implements Subject {

	private Subject realSubject;

	public Proxy(Subject realSubject) {
		this.realSubject = realSubject;
	}

	protected void preRequest() {
		System.out.println("代理对象预请求时操作。");
	}

	protected void postRequest() {
		System.out.println("代理对象请求提交后的操作。");
	}

	@Override
	public void request() {
		preRequest();
		this.realSubject.request();
		postRequest();
	}

}
