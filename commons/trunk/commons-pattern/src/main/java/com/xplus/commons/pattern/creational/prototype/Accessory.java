package com.xplus.commons.pattern.creational.prototype;

import java.io.Serializable;

public class Accessory implements Serializable {

	private static final long serialVersionUID = 2135123864105096647L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Accessory [name=" + name + "]";
	}
	
}
