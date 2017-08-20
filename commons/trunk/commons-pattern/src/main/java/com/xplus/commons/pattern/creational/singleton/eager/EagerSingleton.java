package com.xplus.commons.pattern.creational.singleton.eager;

/**
 * <ul>
 * <li>一是某个类只能有一个实例；</li>
 * <li>二是它必须自行创建这个实例；</li>
 * <li>三是它必须自行向整个系统提供这个实例。</li>
 * </ul>
 * @author hunnyhu
 */
public class EagerSingleton {

	private EagerSingleton() {
	}

	private static final EagerSingleton instance = new EagerSingleton();

	public static EagerSingleton getInstance() {
		return instance;
	}

}
