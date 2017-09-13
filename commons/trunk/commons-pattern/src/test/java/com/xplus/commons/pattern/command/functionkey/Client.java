package com.xplus.commons.pattern.command.functionkey;

public class Client {

	public static void main(String[] args) {

		FunctionButton button1 = new FunctionButton("帮助功能键1");
		FunctionButton button2 = new FunctionButton("最小化功能键2");

		Command help = new HelpCommand();
		Command minimize = new MinimizeCommand();

		// 将命令对象注入功能键
		button1.setCommand(help);
		button2.setCommand(minimize);

		SettingWindow settingWindow = new SettingWindow("功能键设置");
		settingWindow.addFunctionButton(button1);
		settingWindow.addFunctionButton(button2);
		settingWindow.display();

		// 调用功能键的业务方法
		button1.onClick();
		button2.onClick();
	}

}
