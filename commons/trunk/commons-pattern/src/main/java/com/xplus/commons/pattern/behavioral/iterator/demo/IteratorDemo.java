package com.xplus.commons.pattern.behavioral.iterator.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class IteratorDemo {
	public static void process(Collection<String> c) {
		Iterator<String> i = c.iterator(); // 创建迭代器对象

		// 通过迭代器遍历聚合对象
		while (i.hasNext()) {
			System.out.println(i.next().toString());
		}
		
	}

	public static void main(String args[]) {
		
		System.out.println("使用ArrayList集合。");
		Collection<String> persons = new ArrayList<String>(); // 创建一个ArrayList类型的聚合对象
		persons.add("张无忌");
		persons.add("小龙女");
		persons.add("令狐冲");
		persons.add("韦小宝");
		persons.add("袁紫衣");
		persons.add("小龙女");

		process(persons);
		
		System.out.println("使用HashSet集合。");
		persons = new HashSet<String>(); // 创建一个ArrayList类型的聚合对象
		persons.add("张无忌");
		persons.add("小龙女");
		persons.add("令狐冲");
		persons.add("韦小宝");
		persons.add("袁紫衣");
		persons.add("小龙女");

		process(persons);
	}
}
