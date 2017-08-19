/**
 * 
 */
package com.xplus.commons.pattern.creational.simplefactory;

/**
 * @author hunnyhu
 *
 */
public abstract class Product {

	public void methodSame() {
		System.out.println("公共方法的实现。");
	}
	
	public abstract void methodDiff();
	
}
