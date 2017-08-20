package com.xplus.commons.pattern.creational.prototype.shallowclone;

import com.xplus.commons.pattern.creational.prototype.Accessory;

public interface Prototype {

	void setAttr(String attr);
	
	String getAttr();
	
	void setAccessory(Accessory accessory);
	
	Accessory getAccessory();
	
	Prototype clone();
	
}
