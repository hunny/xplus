package com.xplus.commons.pattern.creational.prototype.deepclone;

import java.io.IOException;
import java.io.Serializable;

import com.xplus.commons.pattern.creational.prototype.Accessory;

public interface Prototype extends Serializable {

	void setAttr(String attr);
	
	String getAttr();
	
	void setAccessory(Accessory accessory);
	
	Accessory getAccessory();
	
	Prototype deepClone() throws IOException, ClassNotFoundException;
	
}
