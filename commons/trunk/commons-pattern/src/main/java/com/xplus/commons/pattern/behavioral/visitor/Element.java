package com.xplus.commons.pattern.behavioral.visitor;

public interface Element {

	void accept(Visitor visitor);
	
	void display();
	
}
