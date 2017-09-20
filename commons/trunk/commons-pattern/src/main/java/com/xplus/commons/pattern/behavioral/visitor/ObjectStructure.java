package com.xplus.commons.pattern.behavioral.visitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObjectStructure {
	private List<Element> list = new ArrayList<Element>(); // 定义一个集合用于存储元素对象

	public void accept(Visitor visitor) {
		Iterator<Element> element = list.iterator();

		while (element.hasNext()) {
			element.next().accept(visitor); // 遍历访问集合中的每一个元素
		}
	}

	public void addElement(Element element) {
		list.add(element);
	}

	public void removeElement(Element element) {
		list.remove(element);
	}
}
