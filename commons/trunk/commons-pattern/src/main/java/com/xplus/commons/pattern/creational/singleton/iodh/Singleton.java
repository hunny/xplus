package com.xplus.commons.pattern.creational.singleton.iodh;

/**
 * IoDH单例
 * 
 * <p>
 * 在IoDH中，在单例类中增加一个静态(static)内部类，在该内部类中创建单例对象。
 * 
 * <p>
 * 再将该单例对象通过getInstance()方法返回给外部使用。
 * 
 * <p>
 * 由于静态单例对象没有作为Singleton的成员变量直接实例化，
 * <p>
 * 因此类加载时不会实例化Singleton，
 * <p>
 * 第一次调用getInstance()时将加载内部类HolderClass，
 * <p>
 * 在该内部类中定义了一个static类型的变量instance，
 * <p>
 * 此时会首先初始化这个成员变量，
 * <p>
 * 由Java虚拟机来保证其线程安全性，
 * <p>
 * 确保该成员变量只能初始化一次。
 * 
 * @author hunnyhu
 *
 */
public class Singleton {

	private Singleton() {
	}

	private static class HolderClass {
		private final static Singleton instance = new Singleton();
	}

	public static Singleton getInstance() {
		return HolderClass.instance;
	}

}
