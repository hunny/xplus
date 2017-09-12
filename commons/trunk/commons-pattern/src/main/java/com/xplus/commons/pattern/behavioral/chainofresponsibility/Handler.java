package com.xplus.commons.pattern.behavioral.chainofresponsibility;

public interface Handler {
	
	void handleRequest(String request);
	
	void setHandler(Handler handler);

}
