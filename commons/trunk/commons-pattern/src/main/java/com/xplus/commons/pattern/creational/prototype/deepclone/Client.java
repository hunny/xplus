package com.xplus.commons.pattern.creational.prototype.deepclone;

import java.io.IOException;

import com.xplus.commons.pattern.creational.prototype.Accessory;

public class Client {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		System.out.println("Shallow Clone:");
		Prototype obj1 = new ConcretePrototype();
		obj1.setAttr("Sunny");
		Accessory accessory = new Accessory();
		accessory.setName("accessory");
		obj1.setAccessory(accessory);
		System.out.println("obj1:" + obj1.getAttr());
		System.out.println("obj1:" + obj1.getAccessory());
		Prototype obj2 = obj1.deepClone();
		System.out.println("obj2:" + obj2.getAttr());
		System.out.println("obj2:" + obj2.getAccessory());
		// 两个引用类，深复制时是不相同的。
		System.out.println(obj1.getAccessory() == obj2.getAccessory());
	}
}
