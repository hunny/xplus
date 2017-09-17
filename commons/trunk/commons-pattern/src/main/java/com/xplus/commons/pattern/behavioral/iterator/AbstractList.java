package com.xplus.commons.pattern.behavioral.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象聚合类
 * 
 * @param <T>
 */
public abstract class AbstractList<T> {

	protected final List<T> objects = new ArrayList<T>();

	public AbstractList(List<T> objects) {
		this.objects.clear();
		if (null != objects) {
			this.objects.addAll(objects);
		}
	}

	public void add(T obj) {
		this.objects.add(obj);
	}

	public void remove(T obj) {
		this.objects.remove(obj);
	}

	public List<T> gets() {
		return this.objects;
	}

	// 声明创建迭代器对象的抽象工厂方法
	public abstract Iterator<T> createIterator();
}
