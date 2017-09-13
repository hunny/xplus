package com.xplus.commons.pattern.command.functionkey;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能键设置窗口类
 * 
 * @author hunnyhu
 */
public class SettingWindow {

	private String title; // 窗口标题
	// 定义一个List来存储所有功能键
	private List<FunctionButton> functionButtons = new ArrayList<FunctionButton>();

	public SettingWindow(String title) {
		this.title = title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void addFunctionButton(FunctionButton fb) {
		functionButtons.add(fb);
	}

	public void removeFunctionButton(FunctionButton fb) {
		functionButtons.remove(fb);
	}

	// 显示窗口及功能键
	public void display() {
		System.out.println("显示窗口：" + this.title);
		System.out.println("显示功能键：");
		for (FunctionButton button : functionButtons) {
			System.out.println(button.getName());
		}
		System.out.println("------------------------------");
	}
}
