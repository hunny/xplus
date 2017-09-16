package com.xplus.commons.pattern.behavioral.interpreter;

import java.util.HashMap;
import java.util.Map;

public class Context {

	private Map<String, String> map = new HashMap<String, String>();

	public void assign(String key, String value) {
		// 往环境类中设值
		map.put(key, value);
	}

	public String lookup(String key) {
		// 获取存储在环境类中的值
		return map.get(key);
	}

}
