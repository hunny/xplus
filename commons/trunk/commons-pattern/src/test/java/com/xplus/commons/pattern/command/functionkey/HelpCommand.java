package com.xplus.commons.pattern.command.functionkey;

/**
 * 帮助命令类：具体命令类
 * 
 * @author hunnyhu
 */
public class HelpCommand implements Command {

	private HelpHandler handler; // 维持对请求接收者的引用

	public HelpCommand() {
		handler = new HelpHandler();
	}

	@Override
	public void execute() { // 命令执行方法，将调用请求接收者的业务方法
		handler.display();
	}

}
