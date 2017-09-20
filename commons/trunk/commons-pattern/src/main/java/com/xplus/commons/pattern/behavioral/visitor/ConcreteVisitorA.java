package com.xplus.commons.pattern.behavioral.visitor;

public class ConcreteVisitorA implements Visitor {

	@Override
	public void visit(ConcreteElementA element) {
		System.out.println("访问者" + this.getClass().getSimpleName() + "的具体实现业务逻辑。");
		System.out.println("准备调用" + element.getClass().getSimpleName() + "中的方法:");
		element.display();
	}

	@Override
	public void visit(ConcreteElementB element) {
		System.out.println("访问者" + this.getClass().getSimpleName() + "的具体实现业务逻辑。");
		System.out.println("准备调用" + element.getClass().getSimpleName() + "中的方法:");
		element.display();
	}

}
