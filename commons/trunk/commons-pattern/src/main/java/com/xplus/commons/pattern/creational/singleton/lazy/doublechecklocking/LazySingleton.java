package com.xplus.commons.pattern.creational.singleton.lazy.doublechecklocking;

/**
 * <ul>
 * <li>一是某个类只能有一个实例；</li>
 * <li>二是它必须自行创建这个实例；</li>
 * <li>三是它必须自行向整个系统提供这个实例。</li>
 * </ul>
 * 
 * @author hunnyhu
 */
public class LazySingleton {
	
	private static LazySingleton instance = null;

	private LazySingleton() {
	}

	/**
	 * 懒汉式单例类与线程锁定
	 * 
	 * <p>
	 * 双重检查锁定(Double-Check Locking)。使用双重检查锁定实现的懒汉式单例类
	 * @return
	 */
	synchronized public static LazySingleton getInstance() {
		if (instance == null) {
			instance = new LazySingleton();
		}
		return instance;
	}
}
