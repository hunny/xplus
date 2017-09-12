package com.xplus.commons.pattern.structural.flyweight;

import java.util.HashMap;
import java.util.Map;

public class FlyweightFactory {
	// 定义一个HashMap用于存储享元对象，实现享元池
	private Map<String, Flyweight> flyweights = new HashMap<String, Flyweight>();

	public Flyweight getFlyweight(String key) {

		if (flyweights.containsKey(key)) {// 如果对象存在，则直接从享元池获取
			return (Flyweight) flyweights.get(key);
		} else {// 如果对象不存在，先创建一个新的对象添加到享元池中，然后返回
			Flyweight fw = new ConcreteFlyweight(key);
			flyweights.put(key, fw);
			return fw;
		}
	}
}
