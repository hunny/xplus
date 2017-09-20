package com.xplus.commons.pattern.behavioral.visitor.demo;

/**
 * http://www.runoob.com/design-pattern/visitor-pattern.html
 */
public class VisitorPatternDemo {

	public static void main(String[] args) {
		ComputerPart computer = new Computer();
		computer.accept(new ComputerPartDisplayVisitor());
	}

}
