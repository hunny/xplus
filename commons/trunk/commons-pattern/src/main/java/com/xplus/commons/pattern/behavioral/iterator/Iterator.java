package com.xplus.commons.pattern.behavioral.iterator;

/**
 * 抽象迭代器
 */
public interface Iterator<T> {
	
	/**
	 * 移至下一个元素
	 */
	void next(); // 移至下一个元素

	/**
	 * 判断是否为最后一个元素
	 */
	boolean isLast(); // 判断是否为最后一个元素

	/**
	 * 移至上一个元素
	 */
	void previous(); // 移至上一个元素

	/**
	 * 判断是否为第一个元素
	 */
	boolean isFirst(); // 判断是否为第一个元素

	/**
	 * 获取下一个元素
	 */
	T getNext(); // 获取下一个元素

	/**
	 * 获取上一个元素
	 */
	T getPrevious(); // 获取上一个元素
}
