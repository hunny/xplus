package com.xplus.commons.pattern.creational.prototype.shallowclone;

import com.xplus.commons.pattern.creational.prototype.Accessory;

public class Client {

	public static void main(String[] args) {
		System.out.println("Shallow Clone:");
		Prototype obj1 = new ConcretePrototype();
		obj1.setAttr("Sunny");
		Accessory accessory = new Accessory();
		accessory.setName("accessory");
		obj1.setAccessory(accessory);
		System.out.println("obj1:" + obj1.getAttr());
		System.out.println("obj1:" + obj1.getAccessory());
		Prototype obj2 = obj1.clone();
		System.out.println("obj2:" + obj2.getAttr());
		System.out.println("obj2:" + obj2.getAccessory());
		// 两个引用类，浅复制时是相同的。
		System.out.println(obj1.getAccessory() == obj2.getAccessory());
	}
}
