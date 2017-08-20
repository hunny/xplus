package com.xplus.commons.pattern.creational.prototype.common;

public interface Prototype {

	void setAttr(String attr);
	
	String getAttr();
	
	public Prototype clone();

}
