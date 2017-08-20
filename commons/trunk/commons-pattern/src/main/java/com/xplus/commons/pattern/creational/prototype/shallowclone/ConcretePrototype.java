package com.xplus.commons.pattern.creational.prototype.shallowclone;

import com.xplus.commons.pattern.creational.prototype.Accessory;

public class ConcretePrototype implements Prototype, Cloneable {

	private String attr; // 成员属性
	private Accessory accessory;

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getAttr() {
		return this.attr;
	}

	public Accessory getAccessory() {
		return accessory;
	}

	public void setAccessory(Accessory accessory) {
		this.accessory = accessory;
	}

	public Prototype clone() {
		Object object = null;
		try {
			object = super.clone();
		} catch (CloneNotSupportedException exception) {
			System.err.println("Not support cloneable");
		}
		return (Prototype) object;
	}

}
