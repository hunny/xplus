package com.xplus.commons.pattern.behavioral.template;

public abstract class AbstractTemplate {

	public void template() {
		System.out.println("这个是父类的模板方法，子类是：" + name());
	}
	
	protected abstract String name();
	
}
