package com.xplus.commons.pattern.behavioral.chainofresponsibility;

public class ConcreteHandlerA implements Handler {

	private Handler handler;

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void handleRequest(String request) {
		if ("A".equals(request)) {
			System.out.println("A在处理业务。");
		} else if (null != handler) {
			handler.handleRequest(request);
		} else {
			System.out.println("无对应的业务处理者。");
		}
	}

}
