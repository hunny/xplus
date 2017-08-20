package com.xplus.commons.pattern.creational.singleton.lazy;

/**
 * <ul>
 * <li>一是某个类只能有一个实例；</li>
 * <li>二是它必须自行创建这个实例；</li>
 * <li>三是它必须自行向整个系统提供这个实例。</li>
 * </ul>
 * 
 * <p>
 * 如果使用双重检查锁定来实现懒汉式单例类，需要在静态成员变量instance之前增加修饰符volatile。
 * 
 * <p>
 * 被volatile修饰的成员变量可以确保多个线程都能够正确处理，且该代码只能在JDK 1.5及以上版本中才能正确执行。
 * 
 * <p>
 * 由于volatile关键字会屏蔽Java虚拟机所做的一些代码优化，可能会导致系统运行效率降低，因此即使使用双重检查锁定来实现单例模式也不是一种完美的实现方式。
 * 
 * @author hunnyhu
 */
public class LazySingleton {

	private volatile static LazySingleton instance = null;

	private LazySingleton() {
	}

	/**
	 * 懒汉式单例类与线程锁定
	 * 
	 * <p>
	 * 双重检查锁定(Double-Check Locking)。使用双重检查锁定实现的懒汉式单例类
	 * 
	 * @return
	 */
	public static LazySingleton getInstance() {
		// 第一重判断
		if (instance == null) {
			// 锁定代码块
			synchronized (LazySingleton.class) {
				// 第二重判断
				if (instance == null) {
					instance = new LazySingleton(); // 创建单例实例
				}
			}
		}
		return instance;
	}
}
