package com.xplus.commons.pattern.creational.prototype.common;

public class ConcretePrototype implements Prototype {

	private String attr; // 成员属性

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getAttr() {
		return this.attr;
	}

	public Prototype clone() {
		Prototype prototype = new ConcretePrototype(); // 创建新对象
		prototype.setAttr(this.attr);
		return prototype;
	}

	public static void main(String[] args) {
		System.out.println("Common Clone:");
		Prototype obj1 = new ConcretePrototype();
		obj1.setAttr("Sunny");
		System.out.println("obj1:" + obj1.getAttr());
		Prototype obj2 = obj1.clone();
		System.out.println("obj2:" + obj2.getAttr());
	}

}
