package com.xplus.commons.pattern.command.functionkey;

/**
 * 最小化命令类：具体命令类 
 * @author hunnyhu
 */
public class MinimizeCommand implements Command {

	private MinimizeHandler handler; // 维持对请求接收者的引用

	public MinimizeCommand() {
		handler = new MinimizeHandler();
	}

	@Override
	public void execute() { // 命令执行方法，将调用请求接收者的业务方法
		handler.minimize();
	}

}
