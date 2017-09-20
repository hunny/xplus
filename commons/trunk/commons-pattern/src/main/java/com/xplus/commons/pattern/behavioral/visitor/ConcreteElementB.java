package com.xplus.commons.pattern.behavioral.visitor;

public class ConcreteElementB implements Element {

	@Override
	public void accept(Visitor visitor) {
		System.out.println("运行类：" + this.getClass().getSimpleName() + "接受访问。");
		visitor.visit(this);
		System.out.println("访问者" + visitor.getClass().getSimpleName() + "访问完毕。");
		System.out.println("--------------------");
	}

	@Override
	public void display() {
		System.out.println("运行类：" + this.getClass().getSimpleName() + "中的自定义逻辑。");
	}

}
