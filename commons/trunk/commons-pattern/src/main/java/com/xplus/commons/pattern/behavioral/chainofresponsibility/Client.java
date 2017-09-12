package com.xplus.commons.pattern.behavioral.chainofresponsibility;

public class Client {

	public static void main(String[] args) {
		Handler handler1 = new ConcreteHandlerA();
		Handler handler2 = new ConcreteHandlerB();
		Handler handler3 = new ConcreteHandlerC();
		handler1.setHandler(handler2);
		handler2.setHandler(handler3);
		System.out.println("第一次请求C：");
		handler1.handleRequest("C");
		System.out.println("第二次请求B：");
		handler1.handleRequest("B");
		System.out.println("第三次请求A：");
		handler1.handleRequest("A");
		System.out.println("第四次请求D：");
		handler1.handleRequest("D");
	}

}
