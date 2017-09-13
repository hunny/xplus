package com.xplus.commons.pattern.behavioral.command;

public class Client {

	public static void main(String[] args) {
		
		Receiver receiver = new ConcreteReceiver();
		Command command = new ConcreteCommand(receiver);
		
		Invoker invoker = new Invoker(command);
		invoker.call();
	}

}
