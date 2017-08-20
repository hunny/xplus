package com.xplus.commons.pattern.creational.prototype.deepclone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.xplus.commons.pattern.creational.prototype.Accessory;

public class ConcretePrototype implements Prototype {

	private static final long serialVersionUID = -6848170182623508477L;
	
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

	public Prototype deepClone() throws IOException, ClassNotFoundException {
		// 将对象写入流中
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bao);
		oos.writeObject(this);

		// 将对象从流中取出
		ByteArrayInputStream bis = new ByteArrayInputStream(bao.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		return (Prototype) ois.readObject();
	}

}
