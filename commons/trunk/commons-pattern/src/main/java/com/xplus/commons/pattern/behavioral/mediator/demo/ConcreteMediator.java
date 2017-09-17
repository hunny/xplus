package com.xplus.commons.pattern.behavioral.mediator.demo;

/**
 * 具体中介者
 */
public class ConcreteMediator implements Mediator {

	// 维持对各个同事对象的引用
	public Button addButton;
	public List list;
	public TextBox userNameTextBox;
	public ComboBox cb;

	/**
	 * 封装同事对象之间的交互
	 */
	@Override
	public void componentChanged(Component c) {

		if (c == addButton) {// 单击按钮
			System.out.println("--单击增加按钮--");
			list.update();
			cb.update();
			userNameTextBox.update();
		} else if (c == list) {// 从列表框选择客户
			System.out.println("--从列表框选择客户--");
			cb.select();
			userNameTextBox.setText();
		} else if (c == cb) {// 从组合框选择客户
			System.out.println("--从组合框选择客户--");
			cb.select();
			userNameTextBox.setText();
		}
	}

}
