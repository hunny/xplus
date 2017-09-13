package com.xplus.commons.pattern.behavioral.command;

public class ConcreteCommand implements Command {

	private Receiver receiver; //维持一个对请求接收者对象的引用
	
	public ConcreteCommand(Receiver receiver) {
		this.receiver = receiver;
	}
	
	@Override
	public void execute() {
		System.out.println("具体命令执行方法。");
		receiver.action();//调用请求接收者的业务处理方法action() 
		System.out.println("命令接收者执行完毕。");
	}

}
