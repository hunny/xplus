package com.xplus.commons.pattern.behavioral.visitor;

public interface Visitor {

	void visit(ConcreteElementA element);
	
	void visit(ConcreteElementB element);
	
}
